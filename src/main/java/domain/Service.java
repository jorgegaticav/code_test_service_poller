package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.net.URL;
import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {

    Long id;
    String name;
    URL url;
    String status;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastUpdated;

    public Service(String name, URL url, String status, LocalDateTime createdDate, LocalDateTime lastUpdated) {
        this.name = name;
        this.url = url;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    public Service(Long id, String name, URL url, String status, LocalDateTime createdDate, LocalDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    public Service(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
