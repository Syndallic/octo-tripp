package tankAttack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[300];
	private boolean typing = false;
	private int timeout = 0;
	public boolean A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	public boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
	public boolean SHIFT, SPACE, ENTER;
	public boolean UP, DOWN, RIGHT, LEFT;

	public void update() {
		
		if (ENTER) {
			if (timeout == 0)
				typing = !typing;
			timeout++;
		}
		if (!ENTER) {
			timeout = 0;
		}

		SHIFT = keys[KeyEvent.VK_SHIFT];
		SPACE = keys[KeyEvent.VK_SPACE];
		ENTER = keys[KeyEvent.VK_ENTER];
		UP = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		DOWN = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		RIGHT = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		LEFT = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		
		if (typing) {
			a = keys[KeyEvent.VK_A];
			b = keys[KeyEvent.VK_B];
			c = keys[KeyEvent.VK_C];
			d = keys[KeyEvent.VK_D];
			e = keys[KeyEvent.VK_E];
			r = keys[KeyEvent.VK_F];
			f = keys[KeyEvent.VK_G];
			g = keys[KeyEvent.VK_H];
			h = keys[KeyEvent.VK_I];
			j = keys[KeyEvent.VK_J];
			k = keys[KeyEvent.VK_K];
			l = keys[KeyEvent.VK_L];
			m = keys[KeyEvent.VK_M];
			n = keys[KeyEvent.VK_N];
			o = keys[KeyEvent.VK_O];
			p = keys[KeyEvent.VK_P];
			q = keys[KeyEvent.VK_Q];
			r = keys[KeyEvent.VK_R];
			s = keys[KeyEvent.VK_S];
			t = keys[KeyEvent.VK_T];
			u = keys[KeyEvent.VK_U];
			v = keys[KeyEvent.VK_V];
			w = keys[KeyEvent.VK_W];
			x = keys[KeyEvent.VK_X];
			y = keys[KeyEvent.VK_Y];
			z = keys[KeyEvent.VK_Z];

			if (SHIFT) {
				A = keys[KeyEvent.VK_A];
				B = keys[KeyEvent.VK_B];
				C = keys[KeyEvent.VK_C];
				D = keys[KeyEvent.VK_D];
				E = keys[KeyEvent.VK_E];
				F = keys[KeyEvent.VK_F];
				G = keys[KeyEvent.VK_G];
				H = keys[KeyEvent.VK_H];
				I = keys[KeyEvent.VK_I];
				J = keys[KeyEvent.VK_J];
				K = keys[KeyEvent.VK_K];
				L = keys[KeyEvent.VK_L];
				M = keys[KeyEvent.VK_M];
				N = keys[KeyEvent.VK_N];
				O = keys[KeyEvent.VK_O];
				P = keys[KeyEvent.VK_P];
				Q = keys[KeyEvent.VK_Q];
				R = keys[KeyEvent.VK_R];
				S = keys[KeyEvent.VK_S];
				T = keys[KeyEvent.VK_T];
				U = keys[KeyEvent.VK_U];
				V = keys[KeyEvent.VK_V];
				W = keys[KeyEvent.VK_W];
				X = keys[KeyEvent.VK_X];
				Y = keys[KeyEvent.VK_Y];
				Z = keys[KeyEvent.VK_Z];
			}
		}
		
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

}
