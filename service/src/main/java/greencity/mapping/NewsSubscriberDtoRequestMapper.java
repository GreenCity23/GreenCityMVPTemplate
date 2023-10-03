package greencity.mapping;


import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.entity.NewsSubscriber;
import org.springframework.stereotype.Component;

@Component
public class NewsSubscriberDtoRequestMapper {
    /**
     * Method for converting {@link NewsSubscriberRequestDto} into {@link NewsSubscriber}.
     *
     * @param  newsSubscriberRequestDto object to convert.
     * @return converted object.
     *
     * @author Arthur Mkrtchian
     */
    public NewsSubscriber convert(NewsSubscriberRequestDto newsSubscriberRequestDto){
        return new NewsSubscriber().setEmail(newsSubscriberRequestDto.getEmail());
    }
}
