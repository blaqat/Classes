echo "\n--- CREATE USERS ---"
adduser paul  --shell /bin/bash --disabled-password --gecos ""
adduser leto  --shell /bin/bash --disabled-password --gecos ""
adduser vlad  --shell /bin/bash --disabled-password --gecos ""

echo "\n--- CREATE GROUPS ---"
addgroup arrakis
adduser paul arrakis
adduser leto arrakis
# NOTE that vlad is NOT in arrakis

echo "\n--- UMASK ---"
umask

echo "\n--- SET UP DIRECTORY ---"
mkdir /dune
chmod 777 /dune # BAD SECURITY. This is for educational purposes.
ls -la /dune

echo "\n--- SET UP FILES ---"
echo "the spice must flow" >> /dune/melange.txt
chown paul    /dune/melange.txt
chgrp arrakis /dune/melange.txt
chmod g+w     /dune/melange.txt
chmod o-rwx   /dune/melange.txt

mkdir /dune/vault
chmod 771 /dune/vault
chown paul /dune/vault
chgrp arrakis /dune/vault
echo "secrets!" >>    /dune/vault/secret1234.txt
chmod o=rw /dune/vault/secret1234.txt

mkdir /dune/workspace
chgrp arrakis /dune/workspace
chmod g+s /dune/workspace # setgid
touch arrakis /dune/workspace/shared.txt

cp /usr/bin/whoami /dune
mv /dune/whoami /dune/whoamireally
chown paul /dune/whoamireally
chmod u+s  /dune/whoamireally # setuid

echo "\n--- ACT AS PAUL ---"
echo -n "Who am I? "
su paul -c 'whoami'
su paul -c 'umask'
su paul -c 'cat /dune/melange.txt'
su paul -c 'touch         /dune/paul_only.txt'
su paul -c 'chgrp arrakis /dune/paul_only.txt'
su paul -c 'chmod g-rwx   /dune/paul_only.txt'
su paul -c 'echo -n "Running whoamireally: " ; /dune/whoamireally'


su paul -c 'umask 007 ; touch /dune/arrakis_only.txt'
su paul -c 'ls -l /dune/arrakis*'

echo "\n--- ACT AS LETO ---"
echo -n "Who am I? "
su leto -c 'whoami'
su leto -c 'umask'
su leto -c 'touch /dune/leto.txt'
su leto -c 'cat /dune/melange.txt'

su leto -c 'ls /dune/vault'
su leto -c 'cat /dune/vault/secret1234.txt'

su leto -c 'echo -n "Running whoamireally as leto: " ; /dune/whoamireally'

su leto -c 'cat /dune/arrakis_only.txt'

echo "\n--- ACT AS VLAD ---"
echo -n "Who am I? "
su vlad -c 'whoami'
su vlad -c 'umask'
su vlad -c 'touch /dune/vlad.txt'
su vlad -c 'cat /dune/melange.txt'

su vlad -c 'ls /dune/vault'
su vlad -c 'cat /dune/vault/secret1234.txt'

su vlad -c 'echo -n "Running whoamireally as vlad: " ; /dune/whoamireally'

su vlad -c 'cat /dune/arrakis_only.txt'


echo "\n--- PERMISSIONS ---"
ls -l /dune
ls -la /dune/vault
ls -la /dune/workspace
echo "" # newline at the end of the output

# bash
