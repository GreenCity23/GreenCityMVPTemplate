package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.mapping.NewsSubscriberDtoResponseMapper;
import greencity.repository.UserRepo;
import greencity.repository.options.NewsSubscriberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsSubscriberServiceImpl implements NewsSubscriberService {
    private final NewsSubscriberRepo newsSubscriberRepo;
    private final NewsSubscriberDtoResponseMapper mapper;

    @Override
    public List<NewsSubscriberResponseDto> getAllSubscribers() {
        return newsSubscriberRepo.findAll().stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public NewsSubscriberResponseDto saveSubscriber(NewsSubscriberRequestDto newsSubscriberRequestDto) {
        String unsubscriberToken = UUID.randomUUID().toString();
        NewsSubscriber newsSubscriber = newsSubscriberRepo.save(
                new NewsSubscriber()
                        .setEmail(newsSubscriberRequestDto.getEmail())
                        .setUnsubscribeToken(unsubscriberToken)
        );
        return new NewsSubscriberResponseDto()
                .setId(newsSubscriber.getId())
                .setEmail(newsSubscriber.getEmail());
    }

    @Override
    public boolean unsubscribe(String email, String unsubscribeToken) {
        NewsSubscriber subscriber = newsSubscriberRepo.findByEmail(email);
        if (subscriber != null && unsubscribeToken.equals(subscriber.getUnsubscribeToken())) {
            newsSubscriberRepo.deleteSubscriberByToken(unsubscribeToken);
            return true;
        }
        return false;
    }
}
