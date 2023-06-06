package org.salgar.pekko.fsm.base.actors

import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}

import scala.collection.mutable

class ReceptionFacade {
  private val mapUseCaseListing: mutable.Map[String, mutable.Map[ServiceKey[_], Receptionist.Listing]] = new mutable.HashMap

  def put(useCaseKey: String, serviceKey: ServiceKey[_], listing: Receptionist.Listing) : Unit = {
    val useCaseListingOption = mapUseCaseListing.get(useCaseKey)
    if(useCaseListingOption.isDefined) {
      val mapListing : mutable.Map[ServiceKey[_], Receptionist.Listing] = useCaseListingOption.get
      mapListing.put(serviceKey, listing)
    } else {
      val mapListing : mutable.Map[ServiceKey[_], Receptionist.Listing] = new mutable.HashMap
      mapListing.put(serviceKey, listing)
      mapUseCaseListing.put(useCaseKey, mapListing)
    }
  }

  def findActor[T](
                    useCaseKey: String,
                    serviceKey: ServiceKey[T],
                    actorName: String): Option[ActorRef[T]] = {
    val useCaseListingOption = mapUseCaseListing.get(useCaseKey)
    if(useCaseListingOption.isDefined) {
      val receptionListingOption: Option[Receptionist.Listing] = useCaseListingOption.get.get(serviceKey)
      if(receptionListingOption.isDefined) {
        val listingOption = Option(receptionListingOption.get)
        if(listingOption.isDefined) {
          listingOption.get.serviceInstances(serviceKey)
            .foreach(actor =>
              if (actor.path.name == actorName) {
                return Option(actor)
              }
            )
        }
      }
    }
    Option.empty
  }
}