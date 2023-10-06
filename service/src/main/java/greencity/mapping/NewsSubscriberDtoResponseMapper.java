package greencity.mapping;

import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import org.springframework.stereotype.Component;

@Component
public class NewsSubscriberDtoResponseMapper {

    /**
     * Method for converting {@link NewsSubscriber} into {@link NewsSubscriberResponseDto}.
     *
     * @param  newsSubscriber object to convert.
     * @return converted object.
     *
     * @author Arthur Mkrtchian
     */
    public NewsSubscriberResponseDto convert(NewsSubscriber newsSubscriber){
        return new NewsSubscriberResponseDto()
                .setId(newsSubscriber.getId())
                .setEmail(newsSubscriber.getEmail())
                .setConfirmationToken(newsSubscriber.getConfirmationToken())
                .setUnsubscribeToken(newsSubscriber.getUnsubscribeToken());
    }
}
