package com.asf.appcoins.sdk.iab;

import android.support.annotation.NonNull;
import com.asf.appcoins.sdk.iab.entity.SKU;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neuro on 02-03-2018.
 */
public final class SkuManager {

  private final Map<String, SKU> skus;

  public SkuManager(Collection<SKU> skuList) {
    this.skus = new HashMap<>(skuList.size());

    for (SKU sku : skuList) {
      skus.put(sku.getId(), sku);
    }
  }

  public BigDecimal getSkuAmount(String skuId) {
    SKU sku = getSku(skuId);

    return sku.getValue();
  }

  @NonNull public SKU getSku(String skuId) {
    SKU sku = skus.get(skuId);

    if (sku == null) {
      throw new IllegalArgumentException(
          "Can't find Sku. Did you include in the creation process?");
    }

    return sku;
  }

  public Collection<SKU> getSkus() {
    return Collections.unmodifiableCollection(skus.values());
  }
}
