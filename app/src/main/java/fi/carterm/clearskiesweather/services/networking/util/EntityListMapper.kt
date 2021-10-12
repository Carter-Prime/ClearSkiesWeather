package fi.carterm.clearskiesweather.services.networking.util


interface EntityListMapper<Entity, Model> {

        fun mapListFromEntity(entity: Entity): List<Model>

}