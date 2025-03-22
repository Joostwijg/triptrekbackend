package com.triptrek.triptrek.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String mainImage;

    @ElementCollection
    private List<String> galleryImages;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;


    // Getters and Setters

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getMainImage(){return mainImage;}
    public void setMainImage(String mainImage){this.mainImage = mainImage;}

    public List<String> getGalleryImages(){return galleryImages;}
    public void setGalleryImages(List<String> galleryImages){this.galleryImages = galleryImages;}

    public List<Review> getReviews() {return reviews;}
    public void setReviews(List<Review> reviews) {this.reviews = reviews;}

}
