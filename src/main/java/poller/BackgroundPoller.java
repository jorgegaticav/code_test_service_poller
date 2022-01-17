package poller;

import domain.Service;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import repository.ServiceRepository;

import javax.inject.Inject;

public class BackgroundPoller implements IBackgroundPoller{

    private final Logger logger = LoggerFactory.getLogger(BackgroundPoller.class);

    @Inject
    WebClient webClient;

    @Inject
    ServiceRepository serviceRepository;

    @Inject
    public BackgroundPoller() {
    }

    public void pollServices(){
        logger.info("Running polling service...");
        serviceRepository.findAllServices().onSuccess(ar -> ar.forEach(this::pollOneService));
        logger.info("Polling services finished");
    }

    private void pollOneService(Service service){
        webClient.get(service.getUrl().getHost(), service.getUrl().getPath())
                .timeout(8000)
                .send()
                .onSuccess(
                        ar -> {
                            if(ar.statusCode() == 200){
                                serviceRepository.updateServiceStatus(service.getId(), "OK");
                            }
                            else{
                                serviceRepository.updateServiceStatus(service.getId(), "FAIL");
                            }
                        }
                ).onFailure(ar -> {
                    serviceRepository.updateServiceStatus(service.getId(), "FAIL");
                    logger.error(ar.getMessage(), ar.getCause());
                });

    }
}
