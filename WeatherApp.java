package MiNiProject_Java;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class WeatherApp {
    static StringBuilder history = new StringBuilder();

    static class BackgroundPanel extends JPanel {
        Image background;

        public BackgroundPanel() {
            background = new ImageIcon("C:\\Users\\Asus\\Downloads\\vecteezy_realistic-blue-sky-with-flying-birds_13222552_400\\vecteezy_realistic-blue-sky-with-flying-birds_13222552.jpg").getImage(); // your image name
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

        public static void main(String[] args) {

            JFrame frame = new JFrame("Weather App 🌦️");
            JLabel title = new JLabel("Weather App 🌦️", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 22));
            title.setForeground(Color.BLACK);
            title.setOpaque(false);
            frame.getContentPane().setBackground(new Color(100, 180, 255));
            frame.setSize(400, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Input field
            JTextField cityField = new JTextField();
            cityField.setFont(new Font("Arial", Font.BOLD, 16));
            cityField.setPreferredSize(new Dimension(200, 40));
            JButton getWeatherBtn = new JButton("Get Weather");
            JButton historyBtn = new JButton("History");
            JButton resetBtn = new JButton("Reset");


            getWeatherBtn.setFont(new Font("Arial", Font.BOLD, 14));
            getWeatherBtn.setBackground(Color.WHITE);
            getWeatherBtn.setFocusPainted(false);
            // Output area
            JTextPane resultArea = new JTextPane();
            resultArea.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
            resultArea.setBackground(new Color(240, 248, 255));
            resultArea.setForeground(Color.BLACK);
            resultArea.setEditable(false);
            StyledDocument doc = resultArea.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            resultArea.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
            //resultArea.setBackground(new Color(240, 248, 255));
            resultArea.setOpaque(false);
            resultArea.setBackground(new Color(0, 0, 0, 0));
            resultArea.setForeground(Color.BLACK);
            resultArea.setEditable(false);
            getWeatherBtn.addActionListener(e -> {
                String city = cityField.getText().trim().replace(" ", "%20") + ",IN";
                try {
                    String apiKey = "YOUR_API_KEY_HERE";
                    String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                            + city + "&appid=" + apiKey + "&units=metric";

                    java.net.URL url = new java.net.URL(urlString);
                    java.net.HttpURLConnection conn =
                            (java.net.HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

                    java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getInputStream())
                    );

                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    String json = response.toString();

// ✅ Check if city not found
                    if (json.contains("\"cod\":\"404\"") || json.contains("\"cod\":404")) {
                        resultArea.setText("❌ City not found.\nTry like: Nanded,IN or Delhi");
                        return;
                    }

// Extract values
                    String cityName = json.split("\"name\":\"")[1].split("\"")[0];
                    String temp = json.split("\"temp\":")[1].split(",")[0];
                    String humidity = json.split("\"humidity\":")[1].split(",")[0];
                    String weather = json.split("\"description\":\"")[1].split("\"")[0];
                    String wind = json.split("\"speed\":")[1].split(",")[0];
                    String feelsLike = json.split("\"feels_like\":")[1].split(",")[0];
                    String pressure = json.split("\"pressure\":")[1].split(",")[0];
                    String visibility = json.split("\"visibility\":")[1].split(",")[0];
                    String emoji = "";
// Convert weather text to emoji
                    if (weather.contains("clear")) {
                        emoji = "☀️";
                    } else if (weather.contains("cloud")) {
                        emoji = "☁️";
                    } else if (weather.contains("rain")) {
                        emoji = "🌧️";
                    } else if (weather.contains("smoke") || weather.contains("haze")) {
                        emoji = "🌫️";
                    } else if (weather.contains("snow")) {
                        emoji = "❄️";
                    } else {
                        emoji = "🌡️";
                    }

// Format output
                    String output = "📍 City: " + cityName + "\n\n"
                            + "🌡️ Temperature: " + temp + " °C\n"
                            + "🤒 Feels Like: " + feelsLike + " °C\n"
                            + "🌦️ Weather: " + emoji + " " + weather + "\n\n"
                            + "💧 Humidity: " + humidity + " %\n"
                            + "🌪️ Pressure: " + pressure + " hPa\n"
                            + "👁️ Visibility: " + (Integer.parseInt(visibility)/1000) + " km\n"
                            + "🌬️ Wind Speed: " + wind + " m/s";

                    history.append("\n----------------------\n");
                    history.append(output);

                    String space = "\n\n\n\n\n\n\n\n\n"; // adjust this for perfect center
                    resultArea.setText(space + output);

// Apply center again after new text
                    StyledDocument doce = resultArea.getStyledDocument();
                    doce.setParagraphAttributes(0, doc.getLength(), center, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultArea.setText("Error fetching weather!");
                }
            });

            historyBtn.addActionListener(e -> {
                if (history.length() == 0) {
                    resultArea.setText("No history yet!");
                } else {
                    resultArea.setText(history.toString());
                }
            });

            resetBtn.addActionListener(e -> {
                cityField.setText("");          // clear input
                resultArea.setText("");         // clear output
            });

            // Layout

            BackgroundPanel panel = new BackgroundPanel();
            panel.setLayout(new BorderLayout());
            frame.setContentPane(panel);

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(cityField, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(getWeatherBtn);
            buttonPanel.add(historyBtn);
            buttonPanel.add(resetBtn);
            topPanel.add(buttonPanel, BorderLayout.EAST);
            topPanel.setOpaque(false);
            buttonPanel.setOpaque(false);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(title, BorderLayout.SOUTH);
            JScrollPane scrollPane = new JScrollPane(resultArea);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        }
}
