package greencity.dto.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFriendDto {
    private String city;
    private String email;
    private String friendStatus;
    private Long id;
    private Long mutualFriends;
    private String name;
    private String profilePicturePath;
    private Double rating;

    public UserFriendDto(Long id, String name, String email, String city, Double rating, Long mutualFriends,
        String profilePicturePath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.rating = rating;
        this.mutualFriends = mutualFriends;
        this.profilePicturePath = profilePicturePath;
    }
}
