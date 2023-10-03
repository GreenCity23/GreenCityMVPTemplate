package greencity.mapping;


import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.entity.NewsSubscriber;

public class NewsSubscriberDtoRequestMapper {
    /**
     * Method for converting {@link NewsSubscriberRequestDto} into {@link NewsSubscriber}.
     *
     * @param  newsSubscriberRequestDto object to convert.
     * @return converted object.
     *
     * @author Arthur Mkrtchian
     */
    protected NewsSubscriber convert(NewsSubscriberRequestDto newsSubscriberRequestDto){
        return new NewsSubscriber().setEmail(newsSubscriberRequestDto.getEmail());
    }
}
