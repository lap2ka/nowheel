<div align="center">
<img src="src/main/resources/icon.png" alt="" width="256">
<h1>Nowheel</h1>
<p>Nowheel is a simple and lightweight optimization mod for Create, unifying Flywheel and Entity Culling to improve frame rates, reduce micro-stutter, and fix bugs.</p>
<b>New in 2.0: Pop-in and hitches should now be fully gone for most configurations!</b><br>
<small>(Note: Indirect Flywheel is needed for this optimization. Colorwheel 1.3+ is needed if you are using shaders but other versions work)</small>
</div>
<br>
<div align="center">

[![Available on Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/create-nowheel)
[![Available on CurseForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/create-nowheel)

![Available for NeoForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg)
![Available for Fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)
![Available for Forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/forge_vector.svg)

</div>
<hr>

<br>

Nowheel increases performance by improving how Create handles machines that are not visible to the player. **This effect is greatly amplified in bigger factories where not many machines are actually visible.**

**Some of the optimizations that could impact visual fidelity or could have issues are disabled by default.**

Enabling these optimizations in the mod's config should generally be pretty harmless and provide a nice little performance improvement without sacrificing much. **Please report any issues that you experience with these options enabled.**
<br>
<br>
![Nowheel flywheel:indirect comparison](https://cdn.modrinth.com/data/cached_images/9e6f5bca69fbb89b9fb4eaafe32a1ecdc319150a.jpeg)
<hr>

![Nowheel colorwheel:indirect comparison (Colorwheel 1.3.0 beta3)](https://cdn.modrinth.com/data/cached_images/f116b23e90853da86392519bf9b048ee30ef7e20.jpeg)

<ul>
  <li><small>World: Just Create SMP S1</small></li>
  <li><small>Flywheel backends are flywheel:indirect for no shaders and colorwheel:indirect for shaders (Colorwheel 1.3.0 beta3)</small></li>
  <li><small>Tick Culling and Distance Culling are enabled, render distance is at 16</small></li>
</ul>
<hr>

### Nowheel fixes an Entity Culling + Sable bug where players sometimes visually disappear on simulated contraptions
<br>

![Nowheel Entity Culling + Sable disappearing players bug-fix](https://cdn.modrinth.com/data/cached_images/67329bd8d7f0617f899b31093bf2216844db7777.jpeg)
<hr>

## This mod is built on and requires [Entity Culling](https://modrinth.com/mod/entityculling)
## [Create](https://modrinth.com/mod/create) is also required ~~(obviously)~~
# [Colorwheel](https://modrinth.com/mod/colorwheel) is HIGHLY recommended if you use shaders

### License

All code in this repository is licensed under the **MIT** license. You are free to read, distribute and modify the code.

### Credits

Belt and Chain conveyor AABBs are taken from [Create Smart Bounds](https://modrinth.com/mod/create-smart-bounds).