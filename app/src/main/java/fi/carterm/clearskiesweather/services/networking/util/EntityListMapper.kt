package fi.carterm.clearskiesweather.services.networking.util

/**
 *
 * Network Mapper Interface list of objects
 *
 * @author Michael Carter
 * @version 1
 *
 */

interface EntityListMapper<Entity, Model> {

        fun mapListFromEntity(entity: Entity): List<Model>

}