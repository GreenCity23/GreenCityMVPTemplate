package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.CurrentUser;
import greencity.annotations.ImageValidation;
import greencity.constant.HttpStatuses;
import greencity.constant.SwaggerExampleModel;
import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Pageable;
import lombok.*;

import java.util.List;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.EventAttenderDto;
import greencity.dto.event.EventDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {

    /**
     * Method for adding an attender to the event.
     */
    @ApiOperation(value = "Add an attender to the event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @PostMapping(path = "/addAttender/{eventId}",
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<String> addAttender(@PathVariable Long eventId) {
        return null;
    }

    /**
     * Method for adding event to favorites.
     */
    @ApiOperation(value = "Add an event to favorites")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @PostMapping(path = "/addToFavorites/{eventId}",
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Object> addToFavorites(@PathVariable Long eventId) {
        return null;
    }

    /**
     * Method for deleting event.
     */
    @ApiOperation(value = "Delete event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @DeleteMapping(path = "/delete/{eventId}")
    public ResponseEntity<Object> delete(@PathVariable int eventId) {
        return null;
    }

    /**
     * Method for getting all event attenders.
     *
     * @return list of {@link EventAttenderDto} instances.
     */
    @ApiOperation(value = "Get all event attenders")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @GetMapping(path = "/getAllSubscribers/{eventId}")
    public ResponseEntity<List<EventAttenderDto>> getAllEventSubscribers(@PathVariable Long eventId) {
        return null;
    }

    /**
     * Method for getting all event by id.
     *
     * @return {@link EventDto} instance
     */
    @ApiOperation(value = "Get the event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @GetMapping(path = "/event/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable int eventId) {
        return null;
    }

    /**
     * Method for getting all events.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    @ApiOperation(value = "Get all events")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)})
    @GetMapping("")
    @ApiPageable
    public ResponseEntity<PageableAdvancedDto<EventDto>> getEvent(@ApiIgnore Pageable page) {
        // ResponseEntity.status(HttpStatus.OK).body(eventService.findGenericAll(page));
        return null;
    }

    /**
     * Method for getting all events created by user.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    @ApiOperation(value = "Get events created by user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED)})
    @GetMapping("/myEvents/createdEvents")
    @ApiPageable
    public ResponseEntity<PageableAdvancedDto<EventDto>> getEventsCreatedByUser(@ApiIgnore Pageable page) {
        return null;
    }

    /**
     * Method for getting all users events and events which were created by this
     * user.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    @ApiOperation(value = "Get all users events and events which were created by this user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED)})
    @GetMapping("/myEvents/relatedEvents")
    @ApiPageable
    public ResponseEntity<PageableAdvancedDto<EventDto>> getRelatedToUserEvents(@ApiIgnore Pageable page) {
        return null;
    }

    /**
     * Method for getting all users events.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    @ApiOperation(value = "Get all users events")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED)})
    @GetMapping("/myEvents")
    @ApiPageable
    public ResponseEntity<PageableAdvancedDto<EventDto>> getUserEvents(@ApiIgnore Pageable page) {
        return null;
    }

    /**
     * Method for rating event.
     */
    @ApiOperation(value = "Rate event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @PostMapping(path = "/rateEvent/{eventId}/{grade}",
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Object> rateEvent(@PathVariable Long eventId, @PathVariable int grade) {
        return null;
    }

    /**
     * Method for removing an attender from the event.
     */
    @ApiOperation(value = "Remove an attender from the event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @DeleteMapping(path = "/removeAttender/{eventId}")
    public ResponseEntity<Object> removeAttender(@PathVariable Long eventId) {
        return null;
    }

    /**
     * Method for removing an event from favorites.
     *
     */
    @ApiOperation(value = "Remove an event from favorites")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @DeleteMapping(path = "/removeFromFavorites/{eventId}")
    public ResponseEntity<Object> removeFromFavorites(@PathVariable Long eventId) {
        return null;
    }

    /**
     * Method for updating {@link EventVO}.
     *
     * @return dto {@link EventDto} instance.
     */

    @ApiOperation(value = "Update event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK, response = EventDto.class),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})

    @PutMapping(path = "/update",
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventDto> update(
        @ApiParam(value = SwaggerExampleModel.UPDATE_EVENT,
            required = true) @Valid @RequestPart EventVO eventVO,
        @ApiParam(value = "Image of event") @ImageValidation @RequestPart(required = false) MultipartFile image,
        @ApiIgnore @CurrentUser UserVO user) {
        // ResponseEntity.status(HttpStatus.OK).body(eventService.update(eventVO,
        // image, user));
        return null;
    }

}
