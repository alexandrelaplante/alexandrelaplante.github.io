"test" by "Alexandre Laplante"

[--- EXTENTION STUFF ---]

Release along with an interpreter, a "Standard" website.
The story headline is "A BoardHack dungeon"

Include Basic Screen Effects by Emily Short.
Include Plurality by Emily Short.

Include Extended Banner by Stephen Granade.
Rule for printing the banner text: say "" instead.

[Directions]
[A direction can be rpg. North, south, east and west are rpg directions.]
Understand "look around" as looking.
Understand "identify around" as looking.

To decide if (way - a direction) is a listable direction:
	let place be the room way from the location;
	if place is not a room then decide no;
	decide yes.

Definition: A direction is listable if it is a listable direction.
Definition: A thing is listable if it is visible and it is not a choice.

[print json of metadata]
Every turn (this is the metadata rule):
	say "{{{'images': [bracket]";
	let L be the list of visible drawable things;
	let i be 0;
	repeat with target running through L:
		if i is greater than 0, say ","; [no trailing commas]
		increase i by 1;
		say "{
		'name': '[target]',
		'file': '[image of target]',
		'desc': '[description of target]'		
		}";
	say "
	[close bracket],
	'room': '[location]',
	'things': '[list of listable things]',
	'party': '[list of party members]',
	'directions': '[list of listable directions]',
	'actions': 'look, go, identify, damage',
	'choices': [bracket]
	";
	let L be the list of visible choices;
	let i be 0;
	repeat with target running through L:
		if i is greater than 0, say ","; [no trailing commas]
		increase i by 1;
		say "{
		'name': '[target]',
		'desc': '[description of target]'		
		}";
	say "[close bracket]
	}}}";
When play begins: consider the metadata rule;

When play begins: now the command prompt is "";

A thing has a text called image.
Definition: a thing is drawable if their image is not "".

A Being is a kind of person.
A Being has a number called HP.
A Being has a number called MAXHP.
A Being has a number called STR. STR is usually 1.
A Being has a number called INT.
A Being can be hostile or friendly. A Being is usually hostile.
A Being can be aggro. A Being is usually aggro.
Definition: a Being is dead if their HP is less than 1.

A party member is a kind of being.
A party member has a text called INV.

When play begins:
	Repeat with target running through Beings:
		Now the MAXHP of the target is the HP of the target;

When play begins (this is the display party rule):
	Repeat with member running through party member:
		say "
		[bold type][member][roman type]
		[line break]
		Stats: [HP of the member] HP, [STR of the member] STR, [INT of the member] INT.
		[line break]
		Inventory: [INV of the member].
		[line break][line break]";
	wait for any key;
	clear the screen;

Instead of examining yourself: consider the display party rule;

[Identification]
Understand "identify [something]" as identifying.
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

[Choosing]
A choice is a kind of thing. A choice is scenery. A choice can be chosen. A choice is usually not chosen.
Understand "choose [a choice]" as choosing.
Choosing is an action applying to one visible thing.
Carry out choosing a choice (called the answer):
	Now the answer is chosen;
	

[Damaging]
Understand "damage [something] by [number]" as damaging.
Understand the command "d" as "damage".
Damaging is an action applying to one visible thing and one number.
Carry out damaging an object (called the target):
	if the target is a Being:
		say "[The target] takes [the number understood] damage.";
		if the target is friendly:
			Now the target is hostile;
			Say "[The target] is enraged by your unprovoked attack!";
		decrease the HP of the target by the number understood; 
		if the target is dead:
			carry out the killing activity with the target;
	otherwise:
		say "The attack has no effect on [the target].";

[Killing]
Killing something is an activity on beings.
Rule for killing a being (called the target):
	Say "[The target] has been slain!";
	Say "[The target] dropped [list of things carried by the target].";
	Repeat with item running through things carried by the target:
		Say "[The random party member] picks up {[item]}.";
		Now the player carries the item;
	Remove the target from play;

[Monsters attacking]
Every turn (this is the attacking rule):
	Repeat with monster running through hostile aggro visible beings:
		let victim be a random party member;
		say "{!} [The monster] hits [the victim] for [STR of the monster] point[s] of damage!";

Before going:
	Let L be the list of hostile aggro visible beings;
	if the number of entries in L is greater than 1:
		say "You can't go there because [L with indefinite articles] are attacking you!" instead;
	if the number of entries in L is greater than 0:
		say "You can't go there because [L with indefinite articles] [is-are] attacking you!" instead;

[--- GAME STUFF ---]

When play begins:
	say "It is written in the Book of Ishtar:
	[line break][line break]
	After the Creation, the cruel god Moloch rebelled against the authority of Marduk the Creator. Bla bla bla...
	[line break][line break]
	Who reads the stories anyway? Go get the [bold type]Amulet of Yendor[roman type] and come back out alive!";
	wait for any key;
	clear the screen;

The Fighter is a party member.
The HP of the fighter is 10.
STR is 1.
INT is 0.
INV is "{Short Sword} of {Attack}"

The Mage is a party member.
The HP of the mage is 5.
STR is 0.
INT is 1.
INV is "{+2} {Pink Spellbook} of {Fire}".


Entrance is a room. "As you enter the dungeon, the doorway shuts and is sealed tight by a magical force. To the west is the chest room. To the north there is a shop.".
The doorway is scenery in Entrance. the description of the doorway is "There is no way out of here."

Chest room is west of Entrance. "The treasure chest calls to you. To the west is the entrance of this dungeon."
A lizard is here. The description of the lizard is "A weak looking lizard." The lizard is a Being. The HP of the lizard is 5. The image of the lizard is "LizardRed".
A wizard is here. The wizard is a friendly Being. The HP of the wizard is 10. The description of the wizard is "A powerful looking wizard." The image of the wizard is "HumanMage2". The INT of the wizard is 1.

Shop is north of Entrance. "This is the shop. To the south is the entrance to the dungeon.".
A merchant is here. "There is a merchant here who is offering you a choice of items. Your party can only afford one. Will you buy the Red Potion or the Yellow Potion?"
The merchant is a friendly Being. The HP of the merchant is 10. The description of the merchant is "You don't want to mess with this guy." The image of the merchant is "HumanThief2". The STR of the merchant is 2.
The merchant carries a Red Potion. The merchant carries a Yellow Potion.
A choice called c1 is here. "Take the Red Potion".
A choice called c2 is here. "Take the Yellow Potion".
Before choosing c1:
	say "The mage gets the {Red Potion}.";
	Now the player carries the Red Potion;
Before choosing c2:
	say "The mage gets the {Yellow Potion}.";
	Now the player carries the Yellow Potion;
After choosing in Shop:
	remove c1 from play; remove c2 from play;
	now the initial appearance of the merchant is "The merchant is pleased with your choice."

After damaging the merchant:
	remove c1 from play; remove c2 from play;
After killing the merchant:
	say "Well, that was one way of getting the other potion!"