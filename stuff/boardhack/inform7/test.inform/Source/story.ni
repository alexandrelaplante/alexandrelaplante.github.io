"test" by "Alexandre Laplante"

[--- EXTENTION STUFF ---]

Release along with an interpreter, a "Standard" website.
The story headline is "A BoardHack dungeon"

Include Basic Screen Effects by Emily Short.

Include Extended Banner by Stephen Granade.
Rule for printing the banner text:
	say "" instead.
	[say "[bold type][story title][roman type][line break][extended story headline][if story author is not blank] by [story author][end if][line break]Release [release number] / Serial number [story serial number] / Inform 7 build [I7 version number][line break]" instead.]

around is a direction. The opposite of around is around.
A direction can be rpg. Around, north, south, east and west are rpg directions.

Instead of examining around: try looking;

Every turn (this is the metadata rule):
	say "{{{
	'room': '[location]',
	'things': '[list of visible things]',
	'directions': '[list of rpg directions]',
	'actions': 'look, go, take, identify, damage',
	'choices': '[choices]'
	}}}";
When play begins: consider the metadata rule;

When play begins: now the command prompt is "";

Choices is a number that varies. Choices is usually 0.

A Being is a kind of person.
A Being has a number called HP.
A Being has a number called MAXHP.
A Being has a number called STR.
A Being has a number called INT.
A Being can be hostile or friendly. A Being is usually hostile.
A Being can be aggro. A Being is usually aggro.
Definition: a Being is dead if their HP is less than 1.

When play begins:
	Repeat with target running through Beings:
		Now the MAXHP of the target is the HP of the target;

[Identification]
Understand "identify [any thing]" as identifying.
Understand "identify [direction]" as identifying.
Understand the command "id" as "identify".
Identifying is an action applying to one visible thing.
Carry out identifying an object (called the target):
	if the target is a Being:
		say "[the description of the target]
		[the HP of the target]/[the MAXHP of the target] HP,
		[the STR of the target] STR,
		[the INT of the target] INT.";
	otherwise:
		try examining the target;

[Damaging]
Understand "damage [any thing] by [number]" as damaging.
Understand the command "d" as "damage".
Damaging is an action applying to one visible thing and one number.
Carry out damaging an object (called the target):
	if the target is a Being:
		say "[The target] takes [the number understood] damage.";
		if the target is friendly:
			Now the target is hostile;
			Say "[The target] is enraged by your unprovoked attack!";
		decrease the HP of the target by the number understood; 
		if target is dead:
			Remove the target from play;
			Say "[The target] has been slain!";
	otherwise:
		say "The attack has no effect on [the target].";

[Choosing]
Understand "[a number]" as choosing.
Choosing is an action applying to a number.
Carry out choosing:
	if the number understood > choices:
		say "That wasn't one of the choices.";
	now choices is 0; [clear the choices list after choosing]

		

[--- GAME STUFF ---]

When play begins:
	say "
	[bold type]Fighter[roman type]
	[line break]
	Stats: 10 HP, 1 STR, 0 INT.
	[line break]
	Inventory: +0 {Short Sword}.
	[line break][line break]
	[bold type]Mage[roman type]
	[line break]
	Stats: 5 HP, 0 STR, 1 INT.
	[line break]
	Inventory: {+2} {Pink Spellbook} of {Fire}.";
	wait for any key;
	clear the screen;
	say "It is written in the Book of Ishtar:
	[line break][line break]
	After the Creation, the cruel god Moloch rebelled against the authority of Marduk the Creator. Bla bla bla...
	[line break][line break]
	Who reads the stories anyway? Go get the [bold type]Amulet of Yendor[roman type] and come back out alive!";
	wait for any key;
	clear the screen;

Entrance is a room. "As you enter the dungeon, the doorway behind disappears.".
A lizard is here. The lizard is a Being. The HP of the lizard is 5. The description of the lizard is "A weak looking lizard."
A wizard is here. The wizard is a friendly Being. The HP of the wizard is 10. The description of the wizard is "A powerful looking wizard."
The big door is scenery in Entrance. the description of the big door is "There is no way out of here."


Shop is north of Entrance. "There is a merchant here who is offering you a choice of items. Your party can only afford one. Will you buy the red potion or the yellow potion?".
A merchant is here. The merchant is a friendly Being. The HP of the merchant is 10. The description of the merchant is "You don't want to mess with this guy."
Before going to Shop, now choices is 2.
After choosing in Shop:
	if the number understood is 1:
		say "The mage now has a red potion.";
	if the number understood is 2:
		say "The mage now has a yellow potion.".