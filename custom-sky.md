# Custom Planets Sky

Now that you have your beautiful planets registered, your planet need to have a beautiful sky !

## Creation of the Resourcepack

First, you'll need to create the base of the resourcepack. The structure of the resourcepack should look like this :

```
├─ assets
│  └─ [namespace]
│     └─ renderer
│         └─ sky
│
└─ pack.mcmeta
```

::: tip
`[namespace]` should be the same as your datapack
:::


**The sky folder**
This folder will contains all of the custom skies for our planets.

```json
{
  "id": "minecraft:overworld",
  "cloud": true,
  "cloud_height": 192,
  "fog": true,
  "custom_vanilla_objects": {
    "sun": true,
    "sun_texture": "textures/environment/sun.png",
    "sun_height": 300,
    "moon": true,
    "moon_phase": true,
    "moon_texture": "textures/environment/moon_phases.png",
    "moon_height": 75
  },
  "weather": [
    {
      "rain": true,
      "acid_rain": false
    }
  ],
  "sunrise_color": 16755035,
  "stars": {
    "count": 30000,
    "colored": true,
    "all_days_visible": false
  },
  "sky_type": "NORMAL",
  "sky_objects": []
}
```
`id`: The id of the dimension

`cloud`: if the dimension should have cloud

`cloud_height`: The height of the clouds

`fog`: if the dimension should have fog

**custom_vanilla_objects**

`sun`: If there is a sun in the dimension

`sun_texture`: The texture of the sun

`sun_height`: The size of the sun

`moon`: If there is a moon in the dimension

`moon_phase`: If there are moon phases

`moon_texture`: The texture of the moon

`moon_height`: The height of the moon

**Weather**

`rain`: If the rain is render

`acid_rain`: If the rain texture should be the acid one


`sunrise_color`: The color of the sunrise (decimal color)

**Stars**

`count`: The amount of stars

`colored`: If the stars should be colored

`all_days_visible`: If the stars should be rendered even when it's the day


`sky_type`: A don't know but this should stay "NONE"

`sky_objects`: A list of Sky Objects

**Sky Objects**
Sky Objects allows you to add object to the sky.

```json
{
    "texture": "stellaris:textures/environment/solar_system/earth.png",
    "blend": true,
    "size": 8.0,
    "rotation": [
      60.0,
      0.0,
      5.0
    ],
    "rotation_type": "DAY"
}
```

`texture`: The texture of the object

`blend`: If the object should blend

`size`: The size of the object

`rotation`: A Vector3 that is the rotation of the object

`rotation_type`: if the object should move with like the sun. Possibilities : DAY and STATIC


## Examples

You can find exemples in the github of the [Stellaris Project](https://github.com/st0x0ef/stellaris/tree/1.21/common/src/main/resources/assets/stellaris/renderer/sky) 
