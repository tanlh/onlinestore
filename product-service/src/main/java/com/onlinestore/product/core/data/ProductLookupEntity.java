package com.onlinestore.product.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "productlookup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLookupEntity {

    @Id
    String productId;

    @Column(unique = true)
    String title;

}
