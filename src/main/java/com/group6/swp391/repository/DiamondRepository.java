package com.group6.swp391.repository;

import com.group6.swp391.model.Diamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, String> {

    public Diamond getDiamondByDiamondID(String diamondID);

    @Query("SELECT d FROM Diamond d WHERE d.shape = :shape AND ABS(d.dimensions - :dimensions) < 0.1")
    List<Diamond> getByCondition(@Param("shape") String shape,@Param("dimensions") float dimensions);

    @Modifying
    @Query("select max(totalPrice) from Diamond")
    public double getMaxTotalPrice();

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9 order by d.totalPrice asc")
    public List<Diamond> getDiamondBySearchAdvancedByASC(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9 order by d.totalPrice desc")
    public List<Diamond> getDiamondBySearchAdvancedByDESC(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation all is not null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8 and d.shape = ?9")
    public List<Diamond> getDiamondBySearchAdvanced(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.clarify = ?8")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShape(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify);

    // situation shape and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeClarify(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel);

    // situation shape and color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.clarify = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeColor(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String clarify);

    // situation shape and dimensions is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.colorLevel = ?5 and d.clarify = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeDimensions(double priceEnd, double priceBegin, float caratEnd, float caratBegin, char colorLevel, String clarify);

    // situation shape and carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeCarat(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify);

    // situation shape and totalPrice is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeTotalPrice(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify);

    // situation clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.colorLevel = ?7 and d.shape = ?8")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarify(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String shape);

    // situation clarify and color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyColor(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String shape);

    // situation clarify and dimensions is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.colorLevel = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyDimensions(double priceEnd, double priceBegin, float caratEnd, float caratBegin, char colorLevel, String shape);

    // situation clarify and carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyCarat(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, char colorLevel, String shape);

    // situation clarify and totalPrice is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyTotalPrice(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String shape);

    // situation color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6 and d.clarify = ?7 and d.shape = ?8")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColor(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String clarify, String shape);

    // situation color and dimensions is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.clarify = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColorDimensions(double priceEnd, double priceBegin, float caratEnd, float caratBegin, String clarify, String shape);

    // situation color and carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.clarify = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColorCarat(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, String clarify, String shape);

    // situation color and total price is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.clarify = ?5 and d.shape = ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColorTotalPrice(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String clarify, String shape);

    // situation dimensions is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensions(double priceEnd, double priceBegin, float caratEnd, float caratBegin, char colorLevel, String clarify, String shape);

    // situation dimensions and carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.colorLevel = ?3 and d.clarify = ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedDimensionsCarat(double priceEnd, double priceBegin, char colorLevel, String clarify, String shape);

    // situation dimensions and totalPrice is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.colorLevel = ?3 and d.clarify = ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensionsTotalPrice(float caratEnd, float caratBegin, char colorLevel, String clarify, String shape);

    // situation carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCarat(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation carat and total Price is null

    @Modifying
    @Query("select d from Diamond d where d.dimensions <= ?1 and d.dimensions >= ?2 and d.colorLevel = ?3 and d.clarify = ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratTotalPrice(float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation totalPrice is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5 and d.clarify = ?6 and d.shape = ?7")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPrice(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel, String clarify, String shape);

    // situation total price and color and shape is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.clarify = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceColorShape(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String clarify);

    // situation carat and color and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2  and d.dimensions <= ?3 and d.dimensions >= ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratColorClarify(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, String shape);

    // situation carat and color and shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.clarify = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratColorShape(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, String clarify);

    // situation carat and clarify and shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratClarifyShape(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin, char colorLevel);

    // situation size and clarify and shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4  and d.colorLevel = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeSizeClarifyShape(double priceEnd, double priceBegin, float caratEnd, float caratBegin, char colorLevel);

    // situation shape and color and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.dimensions <= ?5 and d.dimensions >= ?6")
    public List<Diamond> getDiamondBySearchAdvancedExcludeShapeColorClarify(double priceEnd, double priceBegin, float caratEnd, float caratBegin, float sizeEnd, float sizeBegin);

    // situation dimensions and color and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensionsColorClarify(double priceEnd, double priceBegin, float caratEnd, float caratBegin, String shape);

    // situation dimensions and color and carat is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.clarify = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensionsColorCarat(double priceEnd, double priceBegin, String clarify, String shape);

    // situation dimensions and totalPrice and carat is null

    @Modifying
    @Query("select d from Diamond d where d.colorLevel = ?1 and d.clarify = ?2 and d.shape = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeDimensionsTotalPriceCarat(char colorLevel, String clarify, String shape);

    // situation carat and dimensions and clarity is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.colorLevel = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratDimensionClarity(double priceEnd, double priceBegin, char colorLevel, String shape);

    // situation carat and dimensions and shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.colorLevel = ?3 and d.clarify = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratDimensionShape(double priceEnd, double priceBegin, char colorLevel, String clarify);

    // situation color and dimensions and shape is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4 and d.clarify = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeColorDimensionShape(double priceEnd, double priceBegin, float caratEnd, float caratBegin, String clarify);

    // situation totalPrice and carat and color is null

    @Modifying
    @Query("select d from Diamond d where d.dimensions <= ?1 and d.dimensions >= ?2 and d.clarify = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratColor(float sizeEnd, float sizeBegin, String clarify, String shape);

    // situation totalPrice and carat and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.dimensions <= ?1 and d.dimensions >= ?2 and d.colorLevel = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratClarify(float sizeEnd, float sizeBegin, char colorLevel, String shape);

    // situation totalPrice and carat and shape is null

    @Modifying
    @Query("select d from Diamond d where d.dimensions <= ?1 and d.dimensions >= ?2 and d.colorLevel = ?3 and d.clarify = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratShape(float sizeEnd, float sizeBegin, char colorLevel, String clarify);

    // situation totalPrice and dimensions and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.colorLevel = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceDimensionsClarify(float caratEnd, float caratBegin, char colorLevel, String shape);

    // situation totalPrice and dimensions and color is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.clarify = ?3 and d.shape = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceColorDimensions(float caratEnd, float caratBegin, String clarify, String shape);

    // situation totalPrice and dimensions and shape is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.colorLevel = ?3 and d.clarify = ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceDimensionsShape(float caratEnd, float caratBegin, char colorLevel, String clarify);

    // situation totalPrice and color and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.shape = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceColorClarify(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, String shape);

    // situation totalPrice and clarify and shape is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4 and d.colorLevel = ?5")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceShapeClarify(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin, char colorLevel);

    // situation totalPrice and carat and dimensions and color is null

    @Modifying
    @Query("select d from Diamond d where d.clarify = ?1 and d.shape = ?2")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratDimensionsColor(String clarify, String shape);

    // situation clarify and carat and dimensions and color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.shape = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyCaratDimensionsColor(double priceEnd, double priceBegin, String shape);

    // situation clarify and shape and dimensions and color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.carat <= ?3 and d.carat >= ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeClarifyShapeDimensionsColor(double priceEnd, double priceBegin, float caratEnd, float caratBegin);

    // situation totalPrice and clarify and dimensions and color is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.shape = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceClarifyDimensionsColor(float caratEnd, float caratBegin, String shape);

    // situation totalPrice and clarify and shape and color is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceClarifyShapeColor(float caratEnd, float caratBegin, float sizeEnd, float sizeBegin);

    // situation totalPrice and clarify and carat and color is null

    @Modifying
    @Query("select d from Diamond d where  d.dimensions <= ?1 and d.dimensions >= ?2  and d.shape = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceClarifyCaratColor(float sizeEnd, float sizeBegin, String shape);

    // situation totalPrice and carat and size and shape is null

    @Modifying
    @Query("select d from Diamond d where d.colorLevel = ?1 and d.clarify = ?2")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratSizeShape(char colorLevel, String clarify);

    // situation totalPrice and carat and size and Clarify is null

    @Modifying
    @Query("select d from Diamond d where d.colorLevel = ?1 and d.shape = ?2")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratSizeClarify(char colorLevel, String shape);

    // situation carat and clarify and shape and color is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.dimensions <= ?3 and d.dimensions >= ?4")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratColorShapeClarify(double priceEnd, double priceBegin, float sizeEnd, float sizeBegin);

    // situation carat and size and shape and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.totalPrice <= ?1 and d.totalPrice >= ?2 and d.colorLevel = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeCaratSizeShapeClarify(double priceEnd, double priceBegin, char colorLevel);

    // situation totalPrice and carat and shape and clarify is null

    @Modifying
    @Query("select d from Diamond d where d.dimensions <= ?1 and d.dimensions >= ?2 and d.colorLevel = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceCaratShapeClarify(float sizeEnd, float sizeBegin, char colorLevel);

    // situation totalPrice and clarify and shape and size is null

    @Modifying
    @Query("select d from Diamond d where d.carat <= ?1 and d.carat >= ?2 and d.colorLevel = ?3")
    public List<Diamond> getDiamondBySearchAdvancedExcludeTotalPriceShapeShapeClarify(float caratEnd, float caratBegin, char colorLevel);

    List<Diamond> findByDiamondNameContaining(String diamondName);

    List<Diamond> findByDiamondIDContaining(String diamondID);
}
