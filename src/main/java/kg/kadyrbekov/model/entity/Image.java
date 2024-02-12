package kg.kadyrbekov.model.entity;

import kg.kadyrbekov.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.MERGE;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String originalFileName;

    private String url;

    private Long size;

    private String contentType;

    private boolean isPreviewImage;

    @Lob
    private byte[] bytes;

    @OneToOne(cascade = {PERSIST,REFRESH,DETACH,MERGE})
    private User user;

    @OneToOne(cascade = {PERSIST,REFRESH,DETACH,MERGE})
    private Club club;

}
