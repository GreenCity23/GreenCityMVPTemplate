package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.service.NewsSubscriberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newsSubscriber")
public class NewsSubscriberController {

    private final NewsSubscriberService newsSubscriberService;

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
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

}
