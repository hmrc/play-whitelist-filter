
play-whitelist-filter
=====================

NOTE v2 onwards is compatable with Play 2.5

This library includes a ```Filter``` for the Play! framework which can be used to block users whose IP addresses are not on a predetermined whitelist.

Currently the only implementation of whitelisting available uses the IP from the ```True-Client-IP``` header provided by Akamai.

Getting Started
--------
1. Import the library:

  ```scala
  resolvers += Resolver.bintrayRepo("hmrc", "releases")
  libraryDependencies ++= Seq(
    "uk.gov.hmrc" %% "play-whitelist-filter" % "2.0.0"
  )
  ```
  
2. Implement the filter trait, for example:

  ```scala
  object WhitelistFilter extends AkamaiWhitelistFilter {
    override val whitelist: Seq[String] = Seq("127.0.0.1")
    override val destination: Call = Call("GET", "https://www.gov.uk")
  }
  ```

3. Add the filter to your ```Application```'s list of filters, for example:

  ```scala
  object MyGlobal extends WithFilters(WhitelistFilter)
  ```

4. Done

---

Bear in mind that as this uses the Akamai ```True-Client-IP``` header, you may wish to exclude the filter from pre-live environments. There are various ways to do this such as only including the filter based on some config field:

```scala
object TestGlobal extends GlobalSettings {

  val myFilters: Seq[Filter] = {
    Seq(SomeFilter, AnotherFilter) ++
    Play.configuration.getBoolean("shouldWhitelist").map {
      _ => Seq(WhitelistFilter)
    }.getOrElse(Seq.empty)
  }

  override def doFilter(next: EssentialAction): EssentialAction = {
    Filters(super.doFilter(next), myFilters: _*)
  }
}
```

You may also wish to exlude certain paths in your application from being filtered such as healthcheck routes. This can be done by implementing the ```excludedPaths: Seq[Call]``` field in the filter:

```scala
object WhitelistFilter extends AkamaiWhitelistFilter {
  override val whitelist: Seq[String] = Seq("127.0.0.1")
  override val destination: Call = Call("GET", "https://www.gov.uk")
  override val excludedPaths: Seq[Call] = Seq(Call("GET", "/healthcheck"))
}
```

===
[![Build Status](https://travis-ci.org/hmrc/play-whitelist-filter.svg?branch=master)](https://travis-ci.org/hmrc/play-whitelist-filter) [ ![Download](https://api.bintray.com/packages/hmrc/releases/play-whitelist-filter/images/download.svg) ](https://bintray.com/hmrc/releases/play-whitelist-filter/_latestVersion)
