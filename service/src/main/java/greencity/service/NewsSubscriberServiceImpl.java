package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.mapping.NewsSubscriberDtoResponseMapper;
import greencity.repository.NewsSubscriberRepo;
import greencity.security.jwt.JwtTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import greencity.enums.Role;

/**
 * Implementation of {@link NewsSubscriberService}.
 *
 * @author Arthur Mkrtchian
 */
@Slf4j
@Service
public class NewsSubscriberServiceImpl implements NewsSubscriberService {
    private final NewsSubscriberRepo newsSubscriberRepo;
    private final NewsSubscriberDtoResponseMapper mapper;
    private final RestTemplate restTemplate;
    private final String userServerLink;

    private final JwtTool jwtTool;

    @Autowired
    public NewsSubscriberServiceImpl(NewsSubscriberRepo newsSubscriberRepo,
                                     NewsSubscriberDtoResponseMapper mapper,
                                     RestTemplate restTemplate,
                                     JwtTool jwtTool,
                                     @Value("${greencityuser.server.address}") String userServerLink){
        this.newsSubscriberRepo = newsSubscriberRepo;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.jwtTool = jwtTool;
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
        sendConfirmationEmail(newsSubscriber.getEmail(), newsSubscriber.getConfirmationToken());
        return new NewsSubscriberResponseDto()
                .setId(newsSubscriber.getId())
                .setEmail(newsSubscriber.getEmail());
    }

    @Override
    public boolean unsubscribe(String email, String unsubscribeToken) {
        NewsSubscriber subscriber = newsSubscriberRepo.findByEmail(email);
        if (subscriber != null && unsubscribeToken.equals(subscriber.getUnsubscribeToken())) {
            newsSubscriberRepo.deleteSubscriberByEmailAndToken(email, unsubscribeToken);
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmSubscription(String email, String confirmationToken) {
        NewsSubscriber subscriber = newsSubscriberRepo.findByEmail(email);
        if (subscriber != null && subscriber.getConfirmationToken().equals(confirmationToken)){
            newsSubscriberRepo.confirmSubscriber(email);
            return true;
        }
        return false;
    }

    @Override
    public void sendConfirmationEmail(String email, String confirmationToken) {
        String url = userServerLink + "/email/send-confirmation";

        NewsSubscriberResponseDto newsSubscriberResponseDto = mapper.convert(newsSubscriberRepo.findByEmail(email));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = jwtTool.createAccessToken(email, Role.ROLE_ADMIN);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<NewsSubscriberResponseDto> request = new HttpEntity<>(newsSubscriberResponseDto, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send email confirmation");
        }
    }
}
