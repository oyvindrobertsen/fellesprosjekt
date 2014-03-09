#!/usr/bin/env bash

# Pre apt-get stuff
sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password password rootpass'
sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password_again password rootpass'
echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections
sudo add-apt-repository ppa:cwchien/gradle

# apt-get
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_ruby_2_0.sh && bash equip_ruby.sh # ruby
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_java.sh && bash equip_java.sh # java 1.7
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_node.sh && bash equip_node.sh # node
sudo apt-get update
sudo apt-get -y install mysql-server-5.5 gradle

# Run the database scripts
if [ ! -f /var/log/databasesetup ];
then
    touch /var/log/databasesetup
    if [ -f /vagrant/db/base.sql ];
    then
        mysql -uroot -prootpass < /vagrant/db/base.sql
        now=$(date +"%d.%m.%Y %T")
        echo "$now: Base sql script ran" >> /var/log/databasesetup
    fi
    if [ -f /vagrant/db/initial.sql ];
    then
        mysql -uroot -prootpass < /vagrant/db/initial.sql
        now=$(date +"%d.%m.%Y %T")
        echo "$now: Added initial data" >> /var/log/databasesetup
    fi
fi