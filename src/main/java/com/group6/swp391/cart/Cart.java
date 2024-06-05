package com.group6.swp391.cart;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Cart {

    private MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

    public boolean add(Object goods) {
        boolean check = false;
        try {
            if(goods instanceof ProductCustom) {
                ProductCustom productCustom = (ProductCustom) goods;
                this.map.add(productCustom.getProdcutCustomId(), productCustom);
                check = true;
            } else if(goods instanceof Diamond) {
                Diamond diamond = (Diamond)goods;
                this.map.add(diamond.getDiamondID(), diamond);
                check = true;
            } else {
                check = false;
            }
        } catch(Exception e) {
            log.error("Can not add this product");
        }
        return check;
    }

    public boolean remove(Object goods) {
        boolean check = false;
        try {
            if(goods instanceof ProductCustom) {
                ProductCustom productCustom = (ProductCustom) goods;
                this.map.remove(productCustom.getProdcutCustomId(), productCustom);
            } else if(goods instanceof Diamond) {
                Diamond diamond = (Diamond)goods;
                this.map.remove(diamond.getDiamondID(), diamond);
            } else {
                check = false;
            }
        } catch(Exception e) {
            log.error("Can not remove this good");
        }
        return check;
    }

    public int getTotalQuantity() {
        int total = 0;
        try {
            for(String i : this.map.keySet()) {
                Iterator<Object> ite = this.map.get(i).iterator();
                for (Iterator<Object> it = ite; it.hasNext(); ) {
                    Object t = it.next();
                    if(t instanceof ProductCustom) {
                        ProductCustom p = (ProductCustom) t;
                        if(p.getDiamond() != null) {
                            total += 1;
                        }
                        total += 1;
                    } else if(t instanceof Diamond) {
                        Diamond d = (Diamond)t;
                        total += 1;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can not get total quantity");
        }
        return total;
    }

    public double getTotalPrice() {
        double total = 0;
        try {
            for(String i : this.map.keySet()) {
                Iterator<Object> ite = this.map.get(i).iterator();
                for (Iterator<Object> it = ite; it.hasNext(); ) {
                    Object t = it.next();
                    if(t instanceof ProductCustom) {
                        ProductCustom p = (ProductCustom) t;
                        total += p.getTotalPrice();
                    } else if(t instanceof Diamond) {
                        Diamond d = (Diamond)t;
                        total += d.getTotalPrice();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can not get total price");
        }
        return total;
    }

    public List<Diamond> getTotalDiamond() {
        List<Diamond> lists = new ArrayList<>();
        for(String i : this.map.keySet()) {
            Iterator<Object> ite = this.map.get(i).iterator();
            for (Iterator<Object> it = ite; it.hasNext(); ) {
                Object t = it.next();
                if(t instanceof ProductCustom) {
                    ProductCustom p = (ProductCustom) t;
                    if(p.getDiamond() != null) {
                        lists.add(p.getDiamond());
                    }
                } else if(t instanceof Diamond) {
                    Diamond d = (Diamond)t;
                    lists.add(d);
                }
            }
        }
        return lists;
    }

    public List<Object> getTotalGoodsInCart() {
        List<Object> lists = new ArrayList<>();
        for(String i : this.map.keySet()) {
            Iterator<Object> ite = this.map.get(i).iterator();
            for (Iterator<Object> it = ite; it.hasNext(); ) {
                Object t = it.next();
                if(t instanceof ProductCustom) {
                    ProductCustom p = (ProductCustom) t;
                    if(p.getDiamond() != null) {
                        lists.add(p.getDiamond());
                    }
                    lists.add(p.getProduct());
                } else if(t instanceof Diamond) {
                    Diamond d = (Diamond)t;
                    lists.add(d);
                }
            }
        }
        return lists;
    }

    //    public boolean removeDiamondSelectedByProduct(Product product, Diamond selectedDiamond) {
//        boolean check = false;
//        try {
//            if(map.containsKey(product.getProductID())) {
//                product.setDiamond(null);
//                check = true;
//            }
//        } catch(Exception e) {
//            log.error("Can not remove this diamond!!!");
//        }
//        return check;
//    }

}
