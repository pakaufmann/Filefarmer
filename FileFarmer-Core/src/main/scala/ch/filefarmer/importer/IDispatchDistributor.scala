package ch.filefarmer.importer

import akka.routing.LoadBalancer
import akka.actor.Actor

/**
 * trait to implement a dispatch distributor
 */
trait IDispatchDistributor extends Actor with LoadBalancer {
}