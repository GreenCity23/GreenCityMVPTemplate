package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.ApiPageableWithLocale;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.constant.SwaggerExampleModel;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.event.*;
import greencity.dto.user.UserVO;
import greencity.service.EventService;
import greencity.service.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
    private final EventService eventService;
    private final SearchService searchService;

    /**
     * Method for creating Event.
     *
     * @param addEventDtoRequest - dto for creating Event entity.
     * @return dto {@link EventDto} instance.
     * @author Vladyslav Siverskyi.
     */
    @ApiOperation(value = "Create event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
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

    /**
     * Method for adding an attender to the event.
     */
    @ApiOperation(value = "Add an attender to the event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @PostMapping(path = "/addAttender/{eventId}")
    public ResponseEntity<Object> addAttender(@PathVariable Long eventId, @ApiIgnore @CurrentUser UserVO userVO) {
        eventService.addAttenderToEvent(eventId, userVO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for adding event to favorites.
     */
    @ApiOperation(value = "Add an event to favorites")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @PostMapping(path = "/addToFavorites/{eventId}")
    public ResponseEntity<Object> addToFavorites(@PathVariable Long eventId) {
        eventService.addToFavorites(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
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
    public ResponseEntity<Object> delete(@PathVariable Long eventId, @ApiIgnore @CurrentUser UserVO user) {
        eventService.delete(eventId, user);
        return ResponseEntity.status(HttpStatus.OK).build();
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
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllSubscribers(eventId));
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
    @GetMapping(path = "/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findById(eventId));
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
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findAll(page));
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
    public ResponseEntity<PageableAdvancedDto<EventDto>> getEventsCreatedByUser(@ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(eventService.findAllByUser(user, page));
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
    public ResponseEntity<PageableAdvancedDto<EventDto>> getRelatedToUserEvents(@ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findAllRelatedToUser(user.getId(), page));
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
    public ResponseEntity<PageableAdvancedDto<EventDto>> getUserEvents(@ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findAllByAttenderId(user.getId(), page));
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
    public ResponseEntity<Object> removeAttender(@PathVariable Long eventId, @ApiIgnore @CurrentUser UserVO userVO) {
        eventService.removeAttenderFromEvent(eventId, userVO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for removing an event from favorites.
     */
    @ApiOperation(value = "Remove an event from favorites")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    @DeleteMapping(path = "/removeFromFavorites/{eventId}")
    public ResponseEntity<Object> removeFromFavorites(@PathVariable Long eventId) {
        eventService.removeFromFavorites(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
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
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventDto> update(
        @ApiParam(value = SwaggerExampleModel.UPDATE_EVENT,
            required = true) @Valid @RequestPart AddEventDtoRequest addEventDtoRequest,
        @ApiParam(value = "Image of event") @RequestPart(required = false) MultipartFile[] images,
        @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.update(addEventDtoRequest, images, user.getId()));
    }

    /**
     * Method for search.
     *
     * @param searchQuery query to search.
     * @return PageableDto of {@link SearchEventDto} instances.
     */
    @ApiOperation(value = "Search Event.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = HttpStatuses.OK),
        @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/search")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<SearchEventDto>> searchEvent(
        @ApiIgnore Pageable pageable,
        @ApiParam(value = "Query to search") @RequestParam String searchQuery) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(eventService.searchEvent(pageable, searchQuery));
    }
}