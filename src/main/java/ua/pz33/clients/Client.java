package ua.pz33.clients;

public class Client {
    private Integer id;
    private Float money;
    private ClientStatus status;

    public Client(int id, float money, ClientStatus status){
        this.id = id;
        this.money = money;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Float getMoney() {
        return money;
    }

    public ClientStatus getStatus() {
        return status;
    }
}

