package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewsSubsciberServiceImpl implements NewsSubscriberService {
    @Override
    public List<NewsSubscriberResponseDto> getAllSubscribers() {
        return null; //TODO
    }

    @Override
    public NewsSubscriberResponseDto saveSubscriber(NewsSubscriberRequestDto newsSubscriberRequestDto) {
        String token = UUID.randomUUID().toString();
        return null; //TODO
    }

    @Override
    public boolean unsubscribe(String unsubscribeToken) {
       return false; //TODO
    }
}
