package tk.divesdk.nutrifood;

public class Alimento {
    private String nome;
    private String nome_cientifico;
    private String nome_popular;
    private String origem;
    private String regiao;
    private String categoria;
    private String caracteristicas;
    private String curiosidade;
    private String culinaria;
    private String energia_kcal;
    private String proteinas_g;
    private String lipideos_g;
    private String carboidratos_g;
    private String fibra_g;
    private String calcio_mg;
    private String fosforo_mg;
    private String ferro_mg;
    private String retinol_mg;
    private String vitb1_mg;
    private String vitb2_mg;
    private String niacina_mg;
    private String vitc_mg;
    private String imagem;

    public Alimento() {
        this.nome = "";
        this.nome_cientifico = "";
        this.nome_popular = "";
        this.origem = "";
        this.regiao = "";
        this.categoria = "";
        this.caracteristicas = "";
        this.curiosidade = "";
        this.culinaria = "";
        this.energia_kcal = "";
        this.proteinas_g = "";
        this.lipideos_g = "";
        this.carboidratos_g = "";
        this.fibra_g = "";
        this.calcio_mg = "";
        this.fosforo_mg = "";
        this.ferro_mg = "";
        this.retinol_mg = "";
        this.vitb1_mg = "";
        this.vitb2_mg = "";
        this.niacina_mg = "";
        this.vitc_mg = "";
        this.imagem = "";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_cientifico() {
        return nome_cientifico;
    }

    public void setNome_cientifico(String nome_cientifico) { this.nome_cientifico = nome_cientifico; }

    public String getNome_popular() {
        return nome_popular;
    }

    public void setNome_popular(String nome_popular) {
        this.nome_popular = nome_popular;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = trimAll(caracteristicas.replace("\n","")); }

    public String getCuriosidade(){
        return curiosidade;
    };

    public void setCuriosidade(String curiosidade){
        this.curiosidade = trimAll(curiosidade.replace("\n",""));
    }

    public String getCulinaria() {
        return culinaria;
    }

    public void setCulinaria(String culinaria) {
        this.culinaria = trimAll(culinaria.replace("\n",""));
    }

    public String getEnergia_kcal() {
        return energia_kcal;
    }

    public void setEnergia_kcal(String energia_kcal) {
        this.energia_kcal = energia_kcal;
    }

    public String getProteinas_g() {
        return proteinas_g;
    }

    public void setProteinas_g(String proteinas_g) {
        this.proteinas_g = proteinas_g;
    }

    public String getLipideos_g() {
        return lipideos_g;
    }

    public void setLipideos_g(String lipideos_g) {
        this.lipideos_g = lipideos_g;
    }

    public String getCarboidratos_g() {
        return carboidratos_g;
    }

    public void setCarboidratos_g(String carboidratos_g) {
        this.carboidratos_g = carboidratos_g;
    }

    public String getFibra_g() {
        return fibra_g;
    }

    public void setFibra_g(String fibra_g) {
        this.fibra_g = fibra_g;
    }

    public String getCalcio_mg() {
        return calcio_mg;
    }

    public void setCalcio_mg(String calcio_mg) {
        this.calcio_mg = calcio_mg;
    }

    public String getFosforo_mg() {
        return fosforo_mg;
    }

    public void setFosforo_mg(String fosforo_mg) {
        this.fosforo_mg = fosforo_mg;
    }

    public String getFerro_mg() {
        return ferro_mg;
    }

    public void setFerro_mg(String ferro_mg) {
        this.ferro_mg = ferro_mg;
    }

    public String getRetinol_mg() {
        return retinol_mg;
    }

    public void setRetinol_mg(String retinol_mg) {
        this.retinol_mg = retinol_mg;
    }

    public String getVitb1_mg() {
        return vitb1_mg;
    }

    public void setVitb1_mg(String vitb1_mg) {
        this.vitb1_mg = vitb1_mg;
    }

    public String getVitb2_mg() {
        return vitb2_mg;
    }

    public void setVitb2_mg(String vitb2_mg) {
        this.vitb2_mg = vitb2_mg;
    }

    public String getNiacina_mg() {
        return niacina_mg;
    }

    public void setNiacina_mg(String niacina_mg) {
        this.niacina_mg = niacina_mg;
    }

    public String getVitc_mg() {
        return vitc_mg;
    }

    public void setVitc_mg(String vitc_mg) {
        this.vitc_mg = vitc_mg;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public static String trimAll(String text){
        String string = text.trim();
        while (string.contains("  ")) {
            string = string.replaceAll("  ", " ");
        }
        return string;
    }
}
