package tk.divesdk.nutrifood;

public class Contato {
    private String email;
    private String assunto;
    private String mensagem;

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato(String pemail, String passunto, String pmensagem) {
        this.email = pemail;
        this.assunto = passunto;
        this.mensagem = pmensagem;
    }
}
