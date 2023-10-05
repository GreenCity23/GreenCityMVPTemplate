package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.mapping.NewsSubscriberDtoResponseMapper;
import greencity.repository.options.NewsSubscriberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class NewsSubscriberServiceImpl implements NewsSubscriberService {
    private final NewsSubscriberRepo newsSubscriberRepo;
    private final NewsSubscriberDtoResponseMapper mapper;
    private final RestTemplate restTemplate;
    private final String userServerLink;

    @Autowired
    public NewsSubscriberServiceImpl(NewsSubscriberRepo newsSubscriberRepo,
                                     NewsSubscriberDtoResponseMapper mapper,
                                     RestTemplate restTemplate,
                                     @Value("${greencityuser.server.address}") String userServerLink){
        this.newsSubscriberRepo = newsSubscriberRepo;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.userServerLink = userServerLink;
    }

    @Override
    public List<NewsSubscriberResponseDto> getAllSubscribers() {
        return newsSubscriberRepo.getAllConfirmed().stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public NewsSubscriberResponseDto saveSubscriber(NewsSubscriberRequestDto newsSubscriberRequestDto) {
        String unsubscribeToken = UUID.randomUUID().toString();
        String confirmationToken = UUID.randomUUID().toString();
        NewsSubscriber newsSubscriber = newsSubscriberRepo.save(
                new NewsSubscriber()
                        .setEmail(newsSubscriberRequestDto.getEmail())
                        .setUnsubscribeToken(unsubscribeToken)
                        .setConfirmationToken(confirmationToken)
        );
        sendConfirmationEmail(newsSubscriber.getEmail(), newsSubscriber.getConfirmationToken()); //TODO check!
        return new NewsSubscriberResponseDto()
                .setId(newsSubscriber.getId())
                .setEmail(newsSubscriber.getEmail());
    }

    @Override
    public boolean unsubscribe(String email, String unsubscribeToken) {
        NewsSubscriber subscriber = newsSubscriberRepo.findByEmail(email);
        if (subscriber != null && unsubscribeToken.equals(subscriber.getUnsubscribeToken())) {
            newsSubscriberRepo.deleteSubscriberByToken(email);
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmSubscription(String email, String confirmationToken) {
        NewsSubscriber subscriber = newsSubscriberRepo.findByEmail(email);
        if (subscriber != null && subscriber.getConfirmationToken().equals(confirmationToken)){
            newsSubscriberRepo.confirmSubscriber(email);
        }
        return false;
    }

    @Override
    public void sendConfirmationEmail(String email, String confirmationToken) {
        String url = userServerLink + "/email/send-confirmation";

        NewsSubscriberResponseDto newsSubscriberResponseDto = new NewsSubscriberResponseDto()
                .setEmail(email)
                .setConfirmationToken(confirmationToken);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, newsSubscriberResponseDto, Object.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send email confirmation");
        }
    }
}
