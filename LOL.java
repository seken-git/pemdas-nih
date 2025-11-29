import java.util.Scanner;

class Location {
    double x, y;

    Location(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class Menu {
    String name;
    int price;

    Menu(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

class OrderDetails {
    Menu menu;
    int quantity;

    OrderDetails(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    int getTotal() {
        return menu.price * quantity;
    }
}

class Driver {
    String name, id;
    Location location;

    Driver(String name, String id, double x, double y) {
        this.name = name;
        this.id = id;
        this.location = new Location(x, y);
    }
}

class Customer {
    String name;
    Location location;
    int balance;
    OrderDetails[] currentOrder = new OrderDetails[50];
    int orderCount = 0;
    String[] history = new String[50];
    int historyCount = 0;
    boolean isCheckout = false;
    String lastDriverName = "";

    Customer(String name, double x, double y) {
        this.name = name;
        this.location = new Location(x, y);
        this.balance = 0;
    }
}

class Pasangan {
    Driver pengemudi;
    double jarak;

    Pasangan(Driver pengemudi, double jarak) {
        this.pengemudi = pengemudi;
        this.jarak = jarak;
    }
}

public class LOL {

    static Driver[] daftarDriver = new Driver[100];
    static int jumlahDriver = 0;
    static Menu[] daftarMenu = new Menu[100];
    static int jumlahMenu = 0;
    static Customer pelangganAktif = null;
    static StringBuilder penampung = new StringBuilder();

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);
        while (pemindai.hasNextLine()) {
            String baris = pemindai.nextLine();
            if (baris.trim().isEmpty())
                break;

            String[] potongan = baris.trim().split("\\s+");
            if (potongan.length > 0) {
                if (pelangganAktif == null) {
                    if (potongan.length >= 3) {
                        try {
                            Double.parseDouble(potongan[1]);
                            pelangganAktif = new Customer(potongan[0], ubahDesimal(potongan[1]),
                                    ubahDesimal(potongan[2]));
                            continue;
                        } catch (Exception e) {
                        }
                    }
                }
                prosesPerintah(potongan);
            }
        }
        System.out.print(penampung);
    }

    static double ubahDesimal(String s) {
        return Double.parseDouble(s);
    }

    static int ubahAngka(String s) {
        return Integer.parseInt(s);
    }

    static void cetak(String s) {
        penampung.append(s).append("\n");
    }

    static void prosesPerintah(String[] p) {
        switch (p[0]) {
            case "UPSERT_CUST":
                perbaruiPelanggan(p);
                break;
            case "DEL_CUST":
                hapusPelanggan(p[1]);
                break;
            case "PRINT_CUST":
                cetakPelanggan();
                break;
            case "UPSERT_DRIVER":
                perbaruiDriver(p);
                break;
            case "DEL_DRIVER":
                hapusDriver(p[1]);
                break;
            case "CALCULATE_DISTANCE":
                hitungJarak(p[1]);
                break;
            case "PRINT_DISTANCE_MATRIX":
                cetakMatriks();
                break;
            case "CALCULATE_COST":
                hitungBiaya(p[1]);
                break;
            case "ADD_MENU":
                tambahMenu(p);
                break;
            case "REMOVE_MENU":
                hapusMenu(p[1]);
                break;
            case "PRINT_MENU":
                cetakMenu();
                break;
            case "ADD_ORDER":
                tambahPesanan(p);
                break;
            case "REMOVE_ORDER":
                hapusPesanan(p);
                break;
            case "PRINT_ORDER":
                cetakPesanan(p[1]);
                break;
            case "CHECKOUT_DELIVER":
                prosesCheckout(p[1]);
                break;
            case "FINISH_DELIVER":
                selesaikanPesanan(p[1]);
                break;
            case "TOPUP":
                isiSaldo(p);
                break;
            case "PRINT_HISTORY":
                cetakRiwayat(p[1]);
                break;
        }
    }

    static void perbaruiPelanggan(String[] data) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(data[1])) {
                pelangganAktif.location = new Location(ubahDesimal(data[2]), ubahDesimal(data[3]));
                cetak("CUSTOMER " + data[1] + " SUDAH ADA, LOKASI BARU @ " + data[2] + ", " + data[3]);
            } else {
                pelangganAktif = new Customer(data[1], ubahDesimal(data[2]), ubahDesimal(data[3]));
                cetak("CUSTOMER " + data[1] + " DITAMBAH @ " + data[2] + ", " + data[3]);
            }
        } else {
            pelangganAktif = new Customer(data[1], ubahDesimal(data[2]), ubahDesimal(data[3]));
            cetak("CUSTOMER " + data[1] + " DITAMBAH @ " + data[2] + ", " + data[3]);
        }
    }

    static void hapusPelanggan(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                pelangganAktif = null;
                cetak("CUSTOMER: " + nama + " BERHASIL DIHAPUS");
            } else {
                cetak("CUSTOMER: " + nama + " TIDAK DITEMUKAN");
            }
        } else {
            cetak("CUSTOMER: " + nama + " TIDAK DITEMUKAN");
        }
    }

    static void cetakPelanggan() {
        if (pelangganAktif != null)
            cetak(pelangganAktif.name + " @ " + pelangganAktif.location.x + ", " + pelangganAktif.location.y);
    }

    static void perbaruiDriver(String[] data) {
        int indeks = 0;
        boolean ditemukan = false;
        while (indeks < jumlahDriver) {
            if (daftarDriver[indeks].name.equals(data[1])) {
                daftarDriver[indeks].id = data[2];
                daftarDriver[indeks].location = new Location(ubahDesimal(data[3]), ubahDesimal(data[4]));
                cetak("DRIVER " + data[1] + " SUDAH ADA, LOKASI BARU @ " + data[3] + ", " + data[4]);
                ditemukan = true;
                break;
            }
            indeks++;
        }
        if (!ditemukan) {
            if (jumlahDriver < 100) {
                daftarDriver[jumlahDriver++] = new Driver(data[1], data[2], ubahDesimal(data[3]), ubahDesimal(data[4]));
                cetak("DRIVER " + data[1] + " DITAMBAH @ " + data[3] + ", " + data[4]);
            }
        }
    }

    static void hapusDriver(String nama) {
        int indeks = 0;
        boolean ditemukan = false;
        while (indeks < jumlahDriver) {
            if (daftarDriver[indeks].name.equals(nama)) {
                int j = indeks;
                if (j < jumlahDriver - 1) {
                    do {
                        daftarDriver[j] = daftarDriver[j + 1];
                        j++;
                    } while (j < jumlahDriver - 1);
                }
                jumlahDriver--;
                cetak("DRIVER: " + nama + " BERHASIL DIHAPUS");
                ditemukan = true;
                break;
            }
            indeks++;
        }
        if (!ditemukan)
            cetak("DRIVER: " + nama + " TIDAK DITEMUKAN");
    }

    static double ambilJarak(Location a, Location b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    static void urutkanPasangan(Pasangan[] p) {
        int n = p.length;
        int i = 0;
        while (i < n - 1) {
            int j = 0;
            do {
                if (p[j].jarak > p[j + 1].jarak) {
                    Pasangan tukar = p[j];
                    p[j] = p[j + 1];
                    p[j + 1] = tukar;
                }
                j++;
            } while (j < n - i - 1);
            i++;
        }
    }

    static void hitungJarak(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                cetak("DISTANCE " + nama + " @ " + pelangganAktif.location.x + ", " + pelangganAktif.location.y);
                Pasangan[] p = new Pasangan[jumlahDriver];
                int i = 0;
                while (i < jumlahDriver) {
                    p[i] = new Pasangan(daftarDriver[i], ambilJarak(pelangganAktif.location, daftarDriver[i].location));
                    i++;
                }
                urutkanPasangan(p);
                i = 0;
                while (i < jumlahDriver) {
                    cetak(p[i].pengemudi.name + " @ " + p[i].pengemudi.location.x + ", " + p[i].pengemudi.location.y
                            + " = " + String.format("%.2f", p[i].jarak));
                    i++;
                }
            }
        }
    }

    static void cetakMatriks() {
        if (pelangganAktif != null) {
            int i = 0;
            if (i < jumlahDriver) {
                do {
                    cetak("[" + pelangganAktif.name + "][" + daftarDriver[i].name + "]["
                            + String.format("%.2f", ambilJarak(pelangganAktif.location, daftarDriver[i].location))
                            + "]");
                    i++;
                } while (i < jumlahDriver);
            }
        }
    }

    static int hitungBiayaKirim(double jarak) {
        int km = (int) Math.ceil(jarak);
        int biaya = 7000;
        if (km > 1) {
            biaya += (Math.min(km, 5) - 1) * 2000;
            if (km > 5) {
                biaya += (Math.min(km, 10) - 5) * 2500;
                if (km > 10) {
                    biaya += (km - 10) * 3000;
                }
            }
        }
        return biaya;
    }

    static void hitungBiaya(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                cetak("DISTANCE " + nama + " @ " + pelangganAktif.location.x + ", " + pelangganAktif.location.y);
                Pasangan[] p = new Pasangan[jumlahDriver];
                int i = 0;
                while (i < jumlahDriver) {
                    p[i] = new Pasangan(daftarDriver[i], ambilJarak(pelangganAktif.location, daftarDriver[i].location));
                    i++;
                }
                urutkanPasangan(p);
                i = 0;
                while (i < jumlahDriver) {
                    cetak(p[i].pengemudi.name + " @ " + p[i].pengemudi.location.x + ", " + p[i].pengemudi.location.y
                            + " = " + hitungBiayaKirim(p[i].jarak));
                    i++;
                }
            }
        }
    }

    static void tambahMenu(String[] data) {
        int i = 0;
        boolean ada = false;
        while (i < jumlahMenu) {
            if (daftarMenu[i].name.equals(data[1])) {
                ada = true;
                break;
            }
            i++;
        }
        if (!ada) {
            if (jumlahMenu < 100)
                daftarMenu[jumlahMenu++] = new Menu(data[1], ubahAngka(data[2]));
        }
    }

    static void hapusMenu(String nama) {
        int i = 0;
        while (i < jumlahMenu) {
            if (daftarMenu[i].name.equals(nama)) {
                int j = i;
                if (j < jumlahMenu - 1) {
                    do {
                        daftarMenu[j] = daftarMenu[j + 1];
                        j++;
                    } while (j < jumlahMenu - 1);
                }
                jumlahMenu--;
                break;
            }
            i++;
        }
    }

    static void cetakMenu() {
        cetak("MENU:");
        int i = 0;
        while (i < jumlahMenu) {
            cetak("- " + daftarMenu[i].name + " " + daftarMenu[i].price);
            i++;
        }
    }

    static void tambahPesanan(String[] data) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(data[1])) {
                Menu menuDipilih = null;
                int i = 0;
                while (i < jumlahMenu) {
                    if (daftarMenu[i].name.equals(data[2])) {
                        menuDipilih = daftarMenu[i];
                        break;
                    }
                    i++;
                }
                if (menuDipilih != null) {
                    boolean ditemukan = false;
                    int j = 0;
                    while (j < pelangganAktif.orderCount) {
                        if (pelangganAktif.currentOrder[j].menu.name.equals(data[2])) {
                            pelangganAktif.currentOrder[j].quantity += ubahAngka(data[3]);
                            ditemukan = true;
                            break;
                        }
                        j++;
                    }
                    if (!ditemukan) {
                        if (pelangganAktif.orderCount < 50)
                            pelangganAktif.currentOrder[pelangganAktif.orderCount++] = new OrderDetails(menuDipilih,
                                    ubahAngka(data[3]));
                    }
                }
            }
        }
    }

    static void hapusPesanan(String[] data) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(data[1])) {
                int i = 0;
                boolean ditemukan = false;
                while (i < pelangganAktif.orderCount) {
                    if (pelangganAktif.currentOrder[i].menu.name.equals(data[2])) {
                        if (pelangganAktif.currentOrder[i].quantity > 1) {
                            pelangganAktif.currentOrder[i].quantity--;
                            cetak(data[2] + " IS DEDUCTED/REMOVED");
                        } else {
                            int j = i;
                            while (j < pelangganAktif.orderCount - 1) {
                                pelangganAktif.currentOrder[j] = pelangganAktif.currentOrder[j + 1];
                                j++;
                            }
                            pelangganAktif.orderCount--;
                            cetak(data[2] + " IS DEDUCTED/REMOVED");
                        }
                        ditemukan = true;
                        break;
                    }
                    i++;
                }
                if (!ditemukan)
                    cetak(data[2] + " IS NOT EXISTS");
            }
        }
    }

    static void cetakPesanan(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                cetak("ORDER " + nama + ":");
                int totalHarga = 0, i = 0;
                while (i < pelangganAktif.orderCount) {
                    int subTotal = pelangganAktif.currentOrder[i].getTotal();
                    totalHarga += subTotal;
                    cetak(pelangganAktif.currentOrder[i].menu.name + " " + pelangganAktif.currentOrder[i].menu.price
                            + " x " + pelangganAktif.currentOrder[i].quantity + " = " + subTotal);
                    i++;
                }
                cetak("TOTAL = " + totalHarga);
            }
        }
    }

    static void prosesCheckout(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                if (pelangganAktif.orderCount > 0) {
                    int totalTagihan = 0, i = 0;
                    do {
                        totalTagihan += pelangganAktif.currentOrder[i].getTotal();
                        i++;
                    } while (i < pelangganAktif.orderCount);

                    if (totalTagihan <= pelangganAktif.balance) {
                        Pasangan[] p = new Pasangan[jumlahDriver];
                        int k = 0;
                        while (k < jumlahDriver) {
                            p[k] = new Pasangan(daftarDriver[k],
                                    ambilJarak(pelangganAktif.location, daftarDriver[k].location));
                            k++;
                        }
                        urutkanPasangan(p);

                        k = 0;
                        boolean ditugaskan = false;
                        while (k < jumlahDriver) {
                            if (p[k].jarak <= 5.0) {
                                if (!p[k].pengemudi.name.equals(pelangganAktif.lastDriverName)) {
                                    pelangganAktif.isCheckout = true;
                                    pelangganAktif.lastDriverName = p[k].pengemudi.name;
                                    cetak("ORDER IS PLACED AND WILL BE DELIVERED BY " + p[k].pengemudi.name + " "
                                            + String.format("%.2f", p[k].jarak) + " KM TO " + nama);
                                    ditugaskan = true;
                                    break;
                                }
                            }
                            k++;
                        }
                        if (!ditugaskan)
                            cetak("NO DRIVER NEARBY AVAILABLE");
                    } else {
                        cetak("BALANCE IS NOT ENOUGH TO PROCEED");
                    }
                } else {
                    cetak("MAKE AN ORDER FIRST");
                }
            }
        }
    }

    static void selesaikanPesanan(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                if (pelangganAktif.orderCount > 0) {
                    if (pelangganAktif.isCheckout) {
                        int totalTagihan = 0, totalItem = 0, i = 0;
                        while (i < pelangganAktif.orderCount) {
                            totalTagihan += pelangganAktif.currentOrder[i].getTotal();
                            totalItem += pelangganAktif.currentOrder[i].quantity;
                            i++;
                        }

                        int nomorUrut = pelangganAktif.historyCount + 1;

                        pelangganAktif.balance -= totalTagihan;

                        if (pelangganAktif.historyCount < 50) {
                            pelangganAktif.history[pelangganAktif.historyCount] = nomorUrut
                                    + ". Date: Tue, 23 Nov 2024 05:29, Items: " + totalItem + ", Total: "
                                    + totalTagihan;
                            pelangganAktif.historyCount++;
                        }

                        pelangganAktif.orderCount = 0;
                        pelangganAktif.isCheckout = false;
                        cetak("FOOD IS DELIVERED TO " + nama + ". BALANCE = " + pelangganAktif.balance);
                    } else {
                        cetak("ORDER IS NOT CHECKED OUT");
                    }
                } else {
                    cetak("MAKE AN ORDER FIRST");
                }
            }
        }
    }

    static void isiSaldo(String[] data) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(data[1])) {
                int saldoLama = pelangganAktif.balance;
                pelangganAktif.balance += ubahAngka(data[2]);
                cetak("BALANCE " + data[1] + " " + saldoLama + " -> " + pelangganAktif.balance);
            }
        }
    }

    static void cetakRiwayat(String nama) {
        if (pelangganAktif != null) {
            if (pelangganAktif.name.equals(nama)) {
                cetak("ORDER HISTORY " + nama);
                int totalKeseluruhan = 0, i = 0;
                while (i < pelangganAktif.historyCount) {
                    cetak(pelangganAktif.history[i]);
                    try {
                        totalKeseluruhan += ubahAngka(pelangganAktif.history[i].split("Total: ")[1]);
                    } catch (Exception e) {
                    }
                    i++;
                }
                cetak("TOTAL: " + totalKeseluruhan);
            }
        }
    }
}
