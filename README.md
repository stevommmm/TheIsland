# The Island #

The island is a special event in many ways, it does a lot of things we wouldn't normally do. In short it gives users full control over a tiny little island, where they have to battle for resources in order to survive. It is best to talk about this event in it's different aspects

Everything outlined in this document is implemented in TheIsland plugin

- [The map](#the_map)
- [The resource chest](#the_chest)
- [Voting](#voting)
- [PvP](#pvp)
- [Scoring](#scoring)

## The Map ##
<a id='the_map'></a>

The map starts off as a small sphere of land hovering in an endless void. There are no boundaries, however no land will ever be generated, all new chunks are generated as air.

The island composition is entirely up to the event administrators, it should at the least contain some lava, water and a food source. The food is an obvious requirement, the lava + water is for a cobblestone generator, which will become important once the existing resources of the island are depleted. If you want to be polite, a sapling or two wouldn't go astray for a renewable wood resource.

The map should provide a solid starting area for users to cooperate to create a habitable island before attempting to extend the island and complete event objectives.

Both the resource chest and the spawn location block are protected by the plugin.


## The Resource Chest ##
<a id='the_chest'></a>

The resource chest is an important item for a public event. Set via configuration at server startup it has a random chance to contain items such as iron, coal and other tools whenever the server reaches 100 deaths or a 1 in 10 chance on each death.

To add added difficulty to this it is suggested that users who die are removed from the game permanently or for a set time period, this option is configurable.


## Voting ##
<a id='voting'></a>

In an unusual twist we are giving users the ability to vote other users off of the server, a majority vote is required for this to be achieved. Once a user is voted off they are gone, forever. All voting is public to everyone on the server, a vote may fail if enough users do not join in the vote in a short period of time after the vote has been cast.

## PvP ##
<a id='pvp'></a>

PvP can be enabled via a configuration value on server startup. Depending on target audience this can add some fun or completely ruin the game if a dominant team takes over the spawn area.

## Scoring ##
<a id='scoring'></a>

TBA - Could feature things such as 'harvest the block at location x,y,z'

