package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="cart")
public class Cart {

    @Id
    private String cartId;
    private Date createdAt;
    @OneToOne
    private User user;

    @OneToMany(mappedBy="cart", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CartItem> items=new ArrayList<>();


}
