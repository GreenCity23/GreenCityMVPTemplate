package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.achievement.AchievementNotification;
import greencity.dto.achievement.AchievementVO;
import greencity.service.AchievementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    private final AchievementService achievementService;


    /**
     * Constructs a new instance of AchievementController with the specified AchievementService as a dependency.
     *
     * @param achievementService the AchievementService to be used by the controller
     */
    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    /**
     * Retrieves all achievements.
     *
     * @return ResponseEntity with a list of AchievementVO objects representing all achievements
     */
    @ApiOperation(value = "Get all achievements.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("")
    public ResponseEntity<List<AchievementVO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.findAll());
    }

    /**
     * Get all the achievements that need to notify for a specific user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing a list of AchievementNotifications
     */
    @ApiOperation(value = "Get all the achievements that need to notify.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/notification/{userId}")
    public ResponseEntity<List<AchievementNotification>> getNotification(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(achievementService.findActiveAchievements(userId));
    }
}