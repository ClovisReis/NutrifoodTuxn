package tk.divesdk.nutrifood;

public class User {
    private String nome;
    private String email;
    private Boolean aprovado;
    private Boolean ofertas;
    private String identificador;

    public User(String pnome, String pemail, Boolean paprovado, Boolean pofertas, String pidentificador) {
        this.nome = pnome;
        this.email = pemail;
        this.aprovado = paprovado;
        this.ofertas = pofertas;
        this.identificador = pidentificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Boolean getOfertas() {
        return ofertas;
    }

    public void setOfertas(Boolean ofertas) {
        this.ofertas = ofertas;
    }
}
