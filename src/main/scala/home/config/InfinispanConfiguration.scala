package home.config

import org.infinispan.Cache
import org.infinispan.manager.DefaultCacheManager

/**
 * Created by aardelean on 13.04.2015.
 */

object InfinispanConfiguration{
  val embeddedCacheManager = new DefaultCacheManager("infinispan.xml")
  def cache = {
    val cache:Cache[String, String] = embeddedCacheManager.getCache("store")
    cache
  }
}

