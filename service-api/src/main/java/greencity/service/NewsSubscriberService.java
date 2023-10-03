package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;

import java.util.List;

public interface NewsSubscriberService {
    /**
     * Method for getting all subscribers.
     *
     * @return list of {@link NewsSubscriberResponseDto} instances.
     *
     * @author Arthur Mkrtchian
     */
    List<NewsSubscriberResponseDto> getAllSubscribers();

    /**
     * Method that add news subscriber.
     *
     * @author Arthur Mkrtchian
     */
    NewsSubscriberResponseDto saveSubscriber(NewsSubscriberRequestDto newsSubscriberRequestDto);


    /**
     * Method that add news subscriber.
     *
     * @author Arthur Mkrtchian
     */
    boolean unsubscribe(String unsubscribeToken);
}
