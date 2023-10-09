package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.service.NewsSubscriberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/newsSubscriber")
public class NewsSubscriberController {

    private final NewsSubscriberService newsSubscriberService;

    @Value("${client.address}")
    private String clientAddress;

    @ApiOperation(value = "Get all subscribers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping
    public ResponseEntity<List<NewsSubscriberResponseDto>> getAllSubscribers() {
        try {
            List<NewsSubscriberResponseDto> subscribers = newsSubscriberService.getAllSubscribers();
            return ResponseEntity.ok(subscribers);
        } catch (Exception e) {
            log.warn("EXCEPTION NY: " + e);
            return ResponseEntity.status(401).build();
        }
    }

    @ApiOperation(value = "Add new subscriber")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
    })
    @PostMapping(consumes = "application/json; charset=utf-8")
    public ResponseEntity<NewsSubscriberRequestDto> saveSubscriber(@RequestBody NewsSubscriberRequestDto dto) {
        try {
            newsSubscriberService.saveSubscriber(dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Unsubscribe user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(
            @RequestParam("email") String email,
            @RequestParam("unsubscribeToken") String unsubscribeToken) {
        try {
            boolean result = newsSubscriberService.unsubscribe(email, unsubscribeToken);
            if (result) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @ApiOperation(value = "Confirm email subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmSubscription(
            @RequestParam("email") String email,
            @RequestParam("confirmationToken") String confirmationToken) {
        try {
            boolean result = newsSubscriberService.confirmSubscription(email, confirmationToken);
            if (result) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", clientAddress);
                return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

}
