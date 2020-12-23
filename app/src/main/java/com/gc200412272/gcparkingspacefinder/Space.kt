package com.gc200412272.gcparkingspacefinder

class Space {
    var id: String? = null
    var parked: Boolean = false
    var time: String? = null

    constructor() {}

    constructor(id: String, parked: Boolean, time: String) {
        this.id = id
        this.parked = parked
        this.time = time
    }
}