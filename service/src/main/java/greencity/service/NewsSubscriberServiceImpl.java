package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewsSubscriberServiceImpl implements NewsSubscriberService {
    @Override
    public List<NewsSubscriberResponseDto> getAllSubscribers() {
        return null; //TODO
    }

    @Override
    public NewsSubscriberResponseDto saveSubscriber(NewsSubscriberRequestDto newsSubscriberRequestDto) {
        String token = UUID.randomUUID().toString();
        return null; //TODO
    }

//    public void addSubscriber(Subscriber subscriber) throws EmailAlreadyExistsException {
//        subscriberRepository.save(subscriber);
//
//    }

    @Override
    public boolean unsubscribe(String unsubscribeToken) {
        return false; //TODO
    }
}
