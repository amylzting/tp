---
layout: page
title: Kai Chao's Project Portfolio Page
---

## Project: Warenager

Warenager is an **inventory application** to help tech-savvy warehouse managers to keep track of items in their warehouse.
It **optimizes management tasks** for warehouse managers including but not exhaustive of updating,
searching and ordering supplies, via Command Line Interface (CLI).

Given below are my contributions to the project.

* **New Feature**: Added the ability to delete existing stock(s) from the inventory.
  * What it does: Allows the user to delete the chosen stock(s).
  * Justification: This feature enables the user to delete any unwanted or unused stock, so that
    the storage will not be clogged up with outdated stock.

* **New Feature**: Added the ability to generate a unique serial number for each newly added stock.
  * What it does: Allows the user to perform commands using serial numbers.
  * Justification: This feature eliminates any confusion that can arise when users reuse field attributes when
   adding a new stock. Users can then differentiate field inputs clearly when using Warenager commands.

* **New Feature**: Added the ability to display various statistics through a pie chart.
  * What it does: Allows the user to view relevant statistics for their entire inventory.
  * Justification: Often statistics are needed for inventory evaluation. The quick overview that this feature
    provides will help the user obtain the required data for analysis.

* **Reviews & Merging**:
  * Ensured code quality when reviewing pull requests, before approving merge.

* **Project management**:
  * Ensured team meetings cover the requirements for the week.

* **Documentation**:
  * User Guide:
    * Converted the draft user guide content to markdown file format.
    * Updated the documentation for `delete` feature.
    * Updated the documentation for `stats` feature.
  * Developer Guide:
    * Updated NFR and Appendix section for Warenager.
    * Added MSS for `delete` and `stats` feature.
    * Added use cases for `delete` and `stats` feature.
    * Added implementation details of the `stats` feature.
