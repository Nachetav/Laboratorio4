public enum OperationType {
    PURCHASE, //Compra o ingreso de componentes. PUT
    QUERY, //Consulta de disponibilidad. GET
    LEND, //Préstamo de componentes. GET + actualización
    RECEIVE, //Devolución de componentes. GET + actualización
    DISPOSE //Baja por daño, pérdida u obsolescencia. DELETE
}
