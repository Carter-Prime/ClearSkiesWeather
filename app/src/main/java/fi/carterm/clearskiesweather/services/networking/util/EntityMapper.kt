package fi.carterm.clearskiesweather.services.networking.util

/**
 *
 * Network Mapper Interface singler object
 *
 * @author Michael Carter
 * @version 1
 *
 */
interface EntityMapper<Entity, Model> {

    fun mapFromEntity(entity: Entity): Model



}