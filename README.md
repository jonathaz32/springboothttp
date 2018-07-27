Terminal on MAC
1) Enter mysql terminal
	1) For me, it was... "mysql -u root -p"
	2) and then enter mysql password (not MAC OS Admin password)
2) mysql> create database db_ex2;
3) mysql> create user 'ex2user'@'localhost' identified by 'password1';
4) mysql> grant all on db_ex2.* to 'ex2user'@'localhost';
5) exit terminal
