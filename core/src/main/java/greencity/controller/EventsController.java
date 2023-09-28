package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.SwaggerExampleModel;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.user.UserVO;
import greencity.service.EventService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
    private final EventService eventService;

    @PostMapping(value = "/create", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<EventDto> createEvent(
            @ApiParam(value = SwaggerExampleModel.ADD_EVENT) @RequestPart AddEventDtoRequest addEventDtoRequest,
            @ApiParam(value = "Images of event") MultipartFile[] images,
            @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.save(addEventDtoRequest, images, user.getId()));
    }
}
