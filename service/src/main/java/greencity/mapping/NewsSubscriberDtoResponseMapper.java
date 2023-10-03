package greencity.mapping;

import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;

public class NewsSubscriberDtoResponseMapper {

    /**
     * Method for converting {@link NewsSubscriber} into {@link NewsSubscriberResponseDto}.
     *
     * @param  newsSubscriber object to convert.
     * @return converted object.
     *
     * @author Arthur Mkrtchian
     */
    protected NewsSubscriberResponseDto convert(NewsSubscriber newsSubscriber){
        return new NewsSubscriberResponseDto()
                .setId(newsSubscriber.getId())
                .setEmail(newsSubscriber.getEmail())
                .setUnsubscribeToken(newsSubscriber.getUnsubscribeToken());
    }
}
