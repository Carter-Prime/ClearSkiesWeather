package fi.carterm.clearskiesweather.services.networking.util

interface EntityMapper<Entity, Model> {

    fun mapFromEntity(entity: Entity): Model



}