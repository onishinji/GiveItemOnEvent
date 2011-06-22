Vocabulaire
=========== 



En deux mots
============

Ce plugin sert � donner des objets � la connexion d'un joueur sur le serveur, le tout param�trable via un fichier XML.

En d�tail
=========

Les objets donn�e au joueurs ne sont pas "give" par magie, ils sont simplement pris d'un coffre X pour le donner � chaque joueurs.
Par exemple, si vous voulez donn� un petit dejeuner tous les matins � vos joueurs voici le contenu du fichier "giveitemonevent/events.xml"


Exemple
=========


<?xml version="1.0" encoding="UTF-8"?>
<events>
	<event>
		<name>breakfast</name>
		<displayName>Petit d�jeuner</displayName>
		<description>Vous �tes un aventurier matinal pour cela Cubes Of legend vous offre un petit d�jeuner</description>
		<is_active>true</is_active>
		<period>monday tuesday wednesday thursday friday saturday sunday</period>
		<hourMin>07</hourMin>
		<hourMax>10</hourMax>
		<items>
			<item>
				<object_id>297</object_id>
				<number>2</number>
				<errorMsg>Il n'y a plus de pain dans les coffres de la boulangerie !</errorMsg>
			</item>
		</items>
	</event>
</events>


Du coup il faut activer les coffres pour cet event, pour cela on utilise la commande in game:
/active-event breakfast

Tous les jours de 7h � 10h le plugin donnera 2 pains � chaque joueurs et affichera le texte <description>. Quand il n'y aura plus le <errorMsg> sera affich�.


