// Cria usuário específico para o serviço de pedidos
db.getSiblingDB('admin').auth('admin', 'admin123');
db.getSiblingDB('db_pedido').createUser({
  user: "pedido_micro",
  pwd: "123_321",
  roles: [
    { role: "readWrite", db: "db_pedido" },
    { role: "dbAdmin", db: "db_pedido" }
  ]
});

// Cria coleções iniciais (opcional)
db.getSiblingDB("db_pedido").createCollection("pedidos");