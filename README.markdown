
# En deux mots
 

Ce plugin sert à donner des objets à la connexion d'un joueur sur le serveur, le tout paramétrable via un fichier XML.

# En détail


Les objets donnée au joueurs ne sont pas "give" par magie, ils sont simplement pris d'un coffre X pour le donner à chaque joueurs.
Par exemple, si vous voulez donner un petit dejeuner tous les matins à vos joueurs voici le contenu du fichier "giveitemonevent/events.xml"


# Exemple


    <?xml version="1.0" encoding="UTF-8"?>
    <events>
        <event>
            <name>breakfast</name>
            <displayName>Petit déjeuner</displayName>
            <description>Vous êtes un aventurier matinal pour cela Cubes Of legend vous offre un petit déjeuner</description>
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

Tous les jours de 7h à 10h le plugin donnera 2 pains à chaque joueurs et affichera le texte <description>. Quand il n'y aura plus le <errorMsg> sera affiché.


