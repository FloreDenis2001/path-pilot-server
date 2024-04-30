package com.mycode.pathpilotserver.image.models;


import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
@Entity(name = "Image")
@ToString
@Builder
public class Image {


    @Id
    @SequenceGenerator(name = "image_sequence", sequenceName = "image_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Column(name="data",length = 150000)
    private byte[] data;

    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL)
    private User user;


}
