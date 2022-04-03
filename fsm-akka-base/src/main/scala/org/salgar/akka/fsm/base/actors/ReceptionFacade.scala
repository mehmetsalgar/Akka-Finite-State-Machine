package org.salgar.akka.fsm.base.actors

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}

import scala.collection.mutable
import scala.collection.mutable.Map

class ReceptionFacade {
  private val mapUseCaseListing: Map[String, Map[ServiceKey[_], Receptionist.Listing]] = new mutable.HashMap

  def put(useCaseKey: String, serviceKey: ServiceKey[_], listing: Receptionist.Listing) : Unit = {
    val useCaseListingOption = mapUseCaseListing.get(useCaseKey)
    if(useCaseListingOption.isDefined) {
      val mapListing : Map[ServiceKey[_], Receptionist.Listing] = useCaseListingOption.get
      mapListing.put(serviceKey, listing)
    } else {
      val mapListing : Map[ServiceKey[_], Receptionist.Listing] = new mutable.HashMap
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